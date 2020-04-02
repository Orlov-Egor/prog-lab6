package server;

import common.exceptions.ClosingNotOpenedServerSocketException;
import common.exceptions.ConnectionErrorException;
import common.exceptions.OpeningServerSocketException;
import common.utility.Outputer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Server implements Runnable {
    private int port;
    private int soTimeout;
    private ServerSocket serverSocket;

    public Server(int port, int soTimeout) {
        this.port = port;
        this.soTimeout = soTimeout;
    }

    public int getPort() {
        return port;
    }

    @Override
    public void run() {
        try {
            openServerSocket();
            while (true) {
                try {
                    Socket clientSocket = connect();
                    processClientRequest(clientSocket);
                } catch (ConnectionErrorException exception) {
                    Outputer.println("Повторное соединение с клиентом...");
                } catch (SocketTimeoutException exception) {
                    break;
                }
            }
        } catch (OpeningServerSocketException exception) {
            Outputer.printerror("Сервер не может быть запущен!");
        }
    }

    public void stop() {
        try {
            if (serverSocket == null) throw new ClosingNotOpenedServerSocketException();
            serverSocket.close();
            Outputer.println("Работа сервера успешно завершена.");
        } catch (ClosingNotOpenedServerSocketException exception) {
            Outputer.printerror("Невозможно завершить работу еще не запущенного сервера!");
        } catch (IOException exception) {
            Outputer.printerror("Произошла ошибка при завершении работы сервера!");
        }
    }

    private void openServerSocket() throws OpeningServerSocketException {
        try {
            Outputer.println("Запускаю сервер на порту " + port + "...");
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

    private Socket connect() throws ConnectionErrorException, SocketTimeoutException {
        try {
            Outputer.println("Прослушиваю порт " + port + "...");
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
            String userRequest;
            String userResponse;
            while (true) {
                userRequest = clientReader.readUTF();
                userResponse = "~" + userRequest + "~";
                Outputer.println("---------------");
                Outputer.println("Полученные данные: " + userRequest);  // Обработка запроса
                clientWriter.writeUTF(userResponse);  // Отправка ответа
                clientWriter.flush();
                Outputer.println("Отправленные данные: " + userResponse);
                Outputer.println("---------------");
            }
        } catch (IOException exception) {
            // Разграничить исключения на отправку ответов клиенту и некорректное чтение при дисконнекте
            Outputer.println("Соединение с клиентом разорвано: " + exception);
        }
    }
}
