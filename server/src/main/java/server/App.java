package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.utility.CollectionFileManager;
import server.utility.CollectionManager;
import server.utility.RequestHandler;

public class App {
    public static final int PORT = 1821;
    public static final int CONNECTION_TIMEOUT = 60*1000;
    public static final String ENV_VARIABLE = "LABA";
    public static Logger logger = LogManager.getLogger("ServerLogger");

    public static void main(String[] args) {
        CollectionFileManager collectionFileManager = new CollectionFileManager(ENV_VARIABLE);
        CollectionManager collectionManager = new CollectionManager(collectionFileManager);
        RequestHandler requestHandler = new RequestHandler(collectionManager);
        Server server = new Server(PORT, CONNECTION_TIMEOUT, requestHandler);
        server.run();
    }
}
