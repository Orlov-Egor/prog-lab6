package client;

import common.utility.Inputer;

public class App {
    public static final String HOST = "localhost";
    public static final int PORT = 1821;
    public static final int RECONNECTION_TIMEOUT = 5*1000;
    public static final int MAX_RECONNECTION_ATTEMPTS = 5;
    public static final String PS1 = "$ ";
    public static final String PS2 = "> ";

    public static void main(String[] args) {
        Client client = new Client(HOST, PORT, RECONNECTION_TIMEOUT, MAX_RECONNECTION_ATTEMPTS);
        Inputer.open();
        client.run();
        Inputer.close();
    }
}
