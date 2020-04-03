package client;

public class App {
    public static final String HOST = "localhost";
    public static final int PORT = 1821;

    public static void main(String[] args) {
        Client client = new Client(HOST, PORT);
        client.run();
    }
}
