package server.utility;

import common.interaction.Request;
import common.interaction.Response;
import common.interaction.ResponseCode;

public class RequestHandler {
    private CollectionManager collectionManager;

    public RequestHandler(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public Response handle(Request request) {
        return new Response(ResponseCode.OK, "test");
    }
}
