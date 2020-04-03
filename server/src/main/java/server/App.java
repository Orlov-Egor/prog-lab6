package server;

public class App {
    public static final int PORT = 1821;
    public static final int CONNECTION_TIMEOUT = 10*1000;

    public static void main(String[] args) {
        Server server = new Server(PORT, CONNECTION_TIMEOUT);
        server.run();
    }
}
