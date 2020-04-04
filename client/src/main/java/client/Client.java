package client;

import common.exceptions.ConnectionErrorException;
import common.interaction.Request;
import common.interaction.Response;
import common.utility.Outputer;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

// TODO: После того, как всё доделаю, разобрать с Object методами во всем проекте

public class Client {
    private final int RECONNECTION_TIMEOUT = 5*1000;
    private final int MAX_RECONNECTION_ATTEMPTS = 5;
    //private final int BUFFER_SIZE = 64;

    private String host;
    private int port;
    private int reconnectionAttempts;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() {
        Outputer.println("Запуск клиента...");
        Outputer.println("Клиент успешно запущен.");
        while (true) {
            try (SocketChannel socketChannel = connectToServer()) {
                processRequestToServer(socketChannel);
            } catch (ConnectionErrorException exception) {
                if (reconnectionAttempts >= MAX_RECONNECTION_ATTEMPTS) break;
                try {
                    Thread.sleep(RECONNECTION_TIMEOUT);
                } catch (IllegalArgumentException timeoutException) {
                    Outputer.printerror("Время ожидания " + RECONNECTION_TIMEOUT +
                            " находится за пределами возможных значений!");
                } catch (Exception timeoutException) {
                    Outputer.printerror("Произошла ошибка при попытке ожидания подключения!\n" +
                            "Повторное подключение будет проведено немедленно.");
                }
                reconnectionAttempts += 1;
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
            int requestNum = 1;
            while (true) {
                // Генерация запроса
                requestToServer = new Request("testCommand"+requestNum, "testArgument"+requestNum, "testObject"+requestNum);
                // Отправка запроса
                serverWriter.writeObject(requestToServer);
                Outputer.println("---------------");
                Outputer.println("Отправленные данные: " + requestToServer);
                // Получение ответа
                serverResponse = (Response) serverReader.readObject();
                // Обработка ответа
                Outputer.println("Полученные данные: " + serverResponse);
                Outputer.println("---------------");
                requestNum++;
                try {
                    Thread.sleep(5 * 1000);
                } catch (Exception exception) {
                    Outputer.printerror("Произошла ошибка при задержке перед следующим запросом!");
                }
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
