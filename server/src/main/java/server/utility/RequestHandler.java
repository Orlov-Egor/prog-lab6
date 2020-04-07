package server.utility;

import common.interaction.Request;
import common.interaction.Response;
import common.interaction.ResponseCode;

public class RequestHandler {
    private Request request;

    public RequestHandler(Request request) {
        this.request = request;
    }

    public Response handle() {
        return new Response(ResponseCode.OK, "test");
    }
}
