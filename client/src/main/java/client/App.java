package client;

import client.utility.MarineAsker;
import client.utility.UserHandler;

import java.util.Scanner;

public class App {
    public static final String PS1 = "$ ";
    public static final String PS2 = "> ";
    public static final String HOST = "localhost";
    public static final int PORT = 1821;
    public static final int RECONNECTION_TIMEOUT = 5*1000;
    public static final int MAX_RECONNECTION_ATTEMPTS = 5;

    public static void main(String[] args) {
        Scanner userScanner = new Scanner(System.in);
        MarineAsker marineAsker = new MarineAsker(userScanner);
        UserHandler userHandler = new UserHandler(userScanner, marineAsker);
        Client client = new Client(HOST, PORT, RECONNECTION_TIMEOUT, MAX_RECONNECTION_ATTEMPTS, userHandler);
        client.run();
        userScanner.close();
    }
}
