package server;

import common.exceptions.ClosingSocketException;
import common.exceptions.ConnectionErrorException;
import common.exceptions.OpeningServerSocketException;
import common.interaction.Request;
import common.interaction.Response;
import common.utility.Outputer;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Server {
    private int port;
    private int soTimeout;
    private ServerSocket serverSocket;

    public Server(int port, int soTimeout) {
        this.port = port;
        this.soTimeout = soTimeout;
    }

    public void run() {
        try {
            openServerSocket();
            while (true) {
                try (Socket clientSocket = connectToClient()) {
                    processClientRequest(clientSocket);
                } catch (ConnectionErrorException exception) {
                    Outputer.println("Повторное соединение с клиентом...");
                } catch (SocketTimeoutException exception) {
                    break;
                } catch (IOException exception) {
                    Outputer.printerror("Произошла ошибка при попытке завершить соединение с клиентом!");
                }
            }
            stop();
        } catch (OpeningServerSocketException exception) {
            Outputer.printerror("Сервер не может быть запущен!");
        }
    }

    private void stop() {
        try {
            if (serverSocket == null) throw new ClosingSocketException();
            serverSocket.close();
            Outputer.println("Работа сервера успешно завершена.");
        } catch (ClosingSocketException exception) {
            Outputer.printerror("Невозможно завершить работу еще не запущенного сервера!");
        } catch (IOException exception) {
            Outputer.printerror("Произошла ошибка при завершении работы сервера!");
        }
    }

    private void openServerSocket() throws OpeningServerSocketException {
        try {
            Outputer.println("Запуск сервера...");
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(soTimeout);
            Outputer.println("Сервер успешно запущен.");
        } catch (IllegalArgumentException exception) {
            Outputer.printerror("Порт " + port + " находится за пределами возможных значений!");
            throw new OpeningServerSocketException();
        } catch (IOException exception) {
            Outputer.printerror("Произошла ошибка при попытке использовать порт " + port + "!");
            throw new OpeningServerSocketException();
        }
    }

    private Socket connectToClient() throws ConnectionErrorException, SocketTimeoutException {
        try {
            Outputer.println("Прослушивание порта " + port + "...");
            Socket clientSocket = serverSocket.accept();
            Outputer.println("Соединение с клиентом успешно установлено.");
            return clientSocket;
        } catch (SocketTimeoutException exception) {
            Outputer.printerror("Превышено время ожидания подключения!");
            throw new SocketTimeoutException();
        } catch (IOException exception) {
            Outputer.printerror("Произошла ошибка при соединении с клиентом!");
            throw new ConnectionErrorException();
        }
    }

    private void processClientRequest(Socket clientSocket) {
        try (ObjectInputStream clientReader = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream clientWriter = new ObjectOutputStream(clientSocket.getOutputStream())) {
            Request userRequest;
            Response responseToUser;
            while (true) {
                // Получение запроса
                userRequest = (Request) clientReader.readObject();
                // Обработка запроса
                Outputer.println("---------------");
                Outputer.println("Полученные данные: " + userRequest);
                // Генерация ответа
                responseToUser = new Response(0, "Command " + userRequest.getCommandName() + "(" + userRequest.getCommandStringArgument() +
                        ", " + userRequest.getCommandObjectArgument() + ") executed.");
                // Отправка ответа
                clientWriter.writeObject(responseToUser);
                clientWriter.flush();
                Outputer.println("Отправленные данные: " + responseToUser);
                Outputer.println("---------------");
            }
        } catch (ClassNotFoundException exception) {
            Outputer.printerror("Произошла ошибка при чтении полученных данных!");
        } catch (InvalidClassException | NotSerializableException exception) {
            Outputer.printerror("Произошла ошибка при отправке данных на клиент!");
        } catch (IOException exception) {
            Outputer.printerror("Непредвиденный разрыв соединения с клиентом! (" + exception + ")");
        }
    }
}
