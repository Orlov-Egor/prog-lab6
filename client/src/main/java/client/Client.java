package client;

import client.utility.UserHandler;
import common.exceptions.ConnectionErrorException;
import common.exceptions.NotInDeclaredLimitsException;
import common.interaction.Request;
import common.interaction.Response;
import common.interaction.ResponseCode;
import common.utility.Outputer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

// TODO: После того, как всё доделаю, разобраться с Object методами во всем проекте

/**
 * Runs the client.
 */
public class Client {
    private String host;
    private int port;
    private int reconnectionTimeout;
    private int reconnectionAttempts;
    private int maxReconnectionAttempts;
    private UserHandler userHandler;

    public Client(String host, int port, int reconnectionTimeout, int maxReconnectionAttempts, UserHandler userHandler) {
        this.host = host;
        this.port = port;
        this.reconnectionTimeout = reconnectionTimeout;
        this.maxReconnectionAttempts = maxReconnectionAttempts;
        this.userHandler = userHandler;
    }

    /**
    * Begins client operation.
    */
    public void run() {
        try {
            boolean processingStatus = true;
            while (processingStatus) {
                try (SocketChannel socketChannel = connectToServer()) {
                    processingStatus = processRequestToServer(socketChannel);
                } catch (ConnectionErrorException exception) {
                    if (reconnectionAttempts >= maxReconnectionAttempts) {
                        Outputer.printerror("Превышено количество попыток подключения!");
                        break;
                    }
                    try {
                        Thread.sleep(reconnectionTimeout);
                    } catch (IllegalArgumentException timeoutException) {
                        Outputer.printerror("Время ожидания подключения '" + reconnectionTimeout +
                                "' находится за пределами возможных значений!");
                        Outputer.println("Повторное подключение будет произведено немедленно.");
                    } catch (Exception timeoutException) {
                        Outputer.printerror("Произошла ошибка при попытке ожидания подключения!");
                        Outputer.println("Повторное подключение будет произведено немедленно.");
                    }
                } catch (IOException exception) {
                    Outputer.printerror("Произошла ошибка при попытке завершить соединение с сервером!");
                }
                reconnectionAttempts++;
            }
            Outputer.println("Работа клиента успешно завершена.");
        } catch (NotInDeclaredLimitsException exception) {
            Outputer.printerror("Клиент не может быть запущен!");
        }
    }

    /**
    * Connecting to server.
    */
    private SocketChannel connectToServer() throws ConnectionErrorException, NotInDeclaredLimitsException {
        try {
            if (reconnectionAttempts >= 1) Outputer.println("Повторное соединение с сервером...");
            SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
            Outputer.println("Соединение с сервером успешно установлено.");
            reconnectionAttempts = 0;
            return socketChannel;
        } catch (IllegalArgumentException exception) {
            Outputer.printerror("Адрес сервера введен некорректно!");
            throw new NotInDeclaredLimitsException();
        } catch (IOException exception) {
            Outputer.printerror("Произошла ошибка при соединении с сервером!");
            throw new ConnectionErrorException();
        }
    }

    /**
    * Server request process.
    */
    private boolean processRequestToServer(SocketChannel socketChannel) {
        Outputer.println("Ожидание разрешения на обмен данными...");
        try (ObjectOutputStream serverWriter = new ObjectOutputStream(socketChannel.socket().getOutputStream());
             ObjectInputStream serverReader = new ObjectInputStream(socketChannel.socket().getInputStream())) {
            Outputer.println("Разрешение на обмен данными получено.");
            Request requestToServer;
            Response serverResponse;
            do {
                requestToServer = userHandler.handle();
                serverWriter.writeObject(requestToServer);
                serverResponse = (Response) serverReader.readObject();
                // TODO: Зачем-то мне все-таки нужен был статус ERROR
                Outputer.print(serverResponse.getResponseBody());
            } while (serverResponse.getResponseCode() != ResponseCode.EXIT);
            return false;
        } catch (InvalidClassException | NotSerializableException exception) {
            Outputer.printerror("Произошла ошибка при отправке данных на сервер!");
        } catch (ClassNotFoundException exception) {
            Outputer.printerror("Произошла ошибка при чтении полученных данных!");
        } catch (IOException exception) {
            Outputer.printerror("Непредвиденный разрыв соединения с сервером!");
        }
        return true;
    }
}
