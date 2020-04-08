package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {
    public static final int PORT = 1821;
    public static final int CONNECTION_TIMEOUT = 60*1000;
    public static Logger logger = LogManager.getLogger("ServerLogger");

    public static void main(String[] args) {
        Server server = new Server(PORT, CONNECTION_TIMEOUT);
        server.run();
    }
}
