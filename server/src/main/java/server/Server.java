package server;

import common.exceptions.ClosingSocketException;
import common.exceptions.ConnectionErrorException;
import common.exceptions.OpeningServerSocketException;
import common.interaction.Request;
import common.interaction.Response;
import common.utility.Outputer;
import server.utility.RequestHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Server {
    private final int soTimeout;

    private int port;
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
                    App.logger.info("Повторное соединение с клиентом...");
                } catch (SocketTimeoutException exception) {
                    break;
                } catch (IOException exception) {
                    Outputer.printerror("Произошла ошибка при попытке завершить соединение с клиентом!");
                    App.logger.error("Произошла ошибка при попытке завершить соединение с клиентом!");
                }
            }
            stop();
        } catch (OpeningServerSocketException exception) {
            Outputer.printerror("Сервер не может быть запущен!");
            App.logger.fatal("Сервер не может быть запущен!");
        }
    }

    private void stop() {
        try {
            if (serverSocket == null) throw new ClosingSocketException();
            serverSocket.close();
            Outputer.println("Работа сервера успешно завершена.");
            App.logger.info("Работа сервера успешно завершена.");
        } catch (ClosingSocketException exception) {
            Outputer.printerror("Невозможно завершить работу еще не запущенного сервера!");
            App.logger.error("Невозможно завершить работу еще не запущенного сервера!");
        } catch (IOException exception) {
            Outputer.printerror("Произошла ошибка при завершении работы сервера!");
            App.logger.error("Произошла ошибка при завершении работы сервера!");
        }
    }

    private void openServerSocket() throws OpeningServerSocketException {
        try {
            Outputer.println("Запуск сервера...");
            App.logger.info("Запуск сервера...");
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(soTimeout);
            Outputer.println("Сервер успешно запущен.");
            App.logger.info("Сервер успешно запущен.");
        } catch (IllegalArgumentException exception) {
            Outputer.printerror("Порт " + port + " находится за пределами возможных значений!");
            App.logger.fatal("Порт " + port + " находится за пределами возможных значений!");
            throw new OpeningServerSocketException();
        } catch (IOException exception) {
            Outputer.printerror("Произошла ошибка при попытке использовать порт " + port + "!");
            App.logger.fatal("Произошла ошибка при попытке использовать порт " + port + "!");
            throw new OpeningServerSocketException();
        }
    }

    private Socket connectToClient() throws ConnectionErrorException, SocketTimeoutException {
        try {
            Outputer.println("Прослушивание порта " + port + "...");
            App.logger.info("Прослушивание порта " + port + "...");
            Socket clientSocket = serverSocket.accept();
            Outputer.println("Соединение с клиентом успешно установлено.");
            App.logger.info("Соединение с клиентом успешно установлено.");
            return clientSocket;
        } catch (SocketTimeoutException exception) {
            Outputer.printerror("Превышено время ожидания подключения!");
            App.logger.warn("Превышено время ожидания подключения!");
            throw new SocketTimeoutException();
        } catch (IOException exception) {
            Outputer.printerror("Произошла ошибка при соединении с клиентом!");
            App.logger.error("Произошла ошибка при соединении с клиентом!");
            throw new ConnectionErrorException();
        }
    }

    private void processClientRequest(Socket clientSocket) {
        try (ObjectInputStream clientReader = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream clientWriter = new ObjectOutputStream(clientSocket.getOutputStream())) {
            Request userRequest;
            Response responseToUser;
            RequestHandler requestHandler;
            while (true) {
                userRequest = (Request) clientReader.readObject();
                Outputer.println("Получен новый запрос.");
                App.logger.info("Получен новый запрос.");
                requestHandler = new RequestHandler(userRequest);
                responseToUser = requestHandler.handle();
                Outputer.println("Запрос успешно обработан.");
                App.logger.info("Запрос успешно обработан.");
                clientWriter.writeObject(responseToUser);
                clientWriter.flush();
                Outputer.println("Отправлен ответ клиенту.");
                App.logger.info("Отправлен ответ клиенту.");
            }
        } catch (ClassNotFoundException exception) {
            Outputer.printerror("Произошла ошибка при чтении полученных данных!");
            App.logger.error("Произошла ошибка при чтении полученных данных!");
        } catch (InvalidClassException | NotSerializableException exception) {
            Outputer.printerror("Произошла ошибка при отправке данных на клиент!");
            App.logger.error("Произошла ошибка при отправке данных на клиент!");
        } catch (IOException exception) {
            Outputer.printerror("Непредвиденный разрыв соединения с клиентом!");
            App.logger.warn("Непредвиденный разрыв соединения с клиентом!");
        }
    }
}
