package client;

import client.utility.UserHandler;
import common.exceptions.ConnectionErrorException;
import common.interaction.Request;
import common.interaction.Response;
import common.utility.Outputer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

// TODO: После того, как всё доделаю, разобраться с Object методами во всем проекте

public class Client {
    private final int reconnectionTimeout;
    private final int maxReconnectionAttempts;
    //private final int BUFFER_SIZE = 64;

    private String host;
    private int port;
    private int reconnectionAttempts = 0;
    private UserHandler userHandler;

    public Client(String host, int port, int reconnectionTimeout, int maxReconnectionAttempts, UserHandler userHandler) {
        this.host = host;
        this.port = port;
        this.reconnectionTimeout = reconnectionTimeout;
        this.maxReconnectionAttempts = maxReconnectionAttempts;
        this.userHandler = userHandler;
    }

    public void run() {
        Outputer.println("Запуск клиента...");
        Outputer.println("Клиент успешно запущен.");
        while (true) {
            try (SocketChannel socketChannel = connectToServer()) {
                processRequestToServer(socketChannel);
            } catch (ConnectionErrorException exception) {
                reconnectionAttempts++;
                if (reconnectionAttempts >= maxReconnectionAttempts) {
                    Outputer.printerror("Превышено количество попыток подключения!");
                    break;
                }
                try {
                    Thread.sleep(reconnectionTimeout);
                } catch (IllegalArgumentException timeoutException) {
                    Outputer.printerror("Время ожидания " + reconnectionTimeout +
                            " находится за пределами возможных значений!");
                } catch (Exception timeoutException) {
                    Outputer.printerror("Произошла ошибка при попытке ожидания подключения!\n" +
                            "Повторное подключение будет проведено немедленно.");
                }
            } catch (IOException exception) {
                Outputer.printerror("Произошла ошибка при попытке завершить соединение с сервером!");
            }
        }
        Outputer.println("Работа клиента успешно завершена.");
    }

    private SocketChannel connectToServer() throws ConnectionErrorException {
        try {
            if (reconnectionAttempts == 0) Outputer.println("Соединение с сервером...");
            else Outputer.println("Повторное соединение с сервером...");
            SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
            // socketChannel.configureBlocking(false);
            Outputer.println("Соединение с сервером успешно установлено.");
            reconnectionAttempts = 0;
            return socketChannel;
        } catch (IllegalArgumentException exception) {
            Outputer.printerror("Адрес сервера введен некорректно!");
            throw new ConnectionErrorException();
        } catch (IOException exception) {
            Outputer.printerror("Произошла ошибка при соединении с сервером!");
            throw new ConnectionErrorException();
        }
    }

    private void processRequestToServer(SocketChannel socketChannel) {
        try (ObjectOutputStream serverWriter = new ObjectOutputStream(socketChannel.socket().getOutputStream());
             ObjectInputStream serverReader = new ObjectInputStream(socketChannel.socket().getInputStream())) {
            Request requestToServer;
            Response serverResponse;
            while (true) {
                // Генерация запроса
                requestToServer = userHandler.handle();
                // Отправка запроса
                serverWriter.writeObject(requestToServer);
                Outputer.println("---------------");
                Outputer.println("Отправленные данные: " + requestToServer);
                // Получение ответа
                serverResponse = (Response) serverReader.readObject();
                // Обработка ответа
                Outputer.println("Полученные данные: " + serverResponse);
                Outputer.println("---------------");
            }
        } catch (InvalidClassException | NotSerializableException exception) {
            Outputer.printerror("Произошла ошибка при отправке данных на сервер!");
        } catch (ClassNotFoundException exception) {
            Outputer.printerror("Произошла ошибка при чтении полученных данных!");
        } catch (IOException exception) {
            Outputer.printerror("Непредвиденный разрыв соединения с сервером!");
        }
    }

//    private void sendRequest(SocketChannel socketChannel, Serializable object) {
//        try {
//            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
//            buffer.put(byteArrayOutputStream.toByteArray());
//            objectOutputStream.writeObject(object);
//            buffer.flip();
//            socketChannel.write(buffer);
//            objectOutputStream.close();
//        } catch (Exception exception) {
//            Outputer.printerror("sendRequest: " + exception);
//        }
//    }
//
//    private Serializable recieveResponse(SocketChannel socketChannel) {
//        try {
//            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
//            while (buffer.position() == 0) {
//                socketChannel.read(buffer);
//            }
//            buffer.flip();
//            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer.array());
//            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
//            objectInputStream.close();
//            return (Serializable) objectInputStream.readObject();
//        } catch (Exception exception) {
//            Outputer.printerror("recieveResponse: " + exception);
//            return null;
//        }
//    }
}
