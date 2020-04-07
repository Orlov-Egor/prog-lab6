package common.interaction;

import java.io.Serializable;

public class Response implements Serializable {
    private ResponseCode responseCode;
    private String responseBody;

    public Response(ResponseCode responseCode, String responseBody) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
    }

    @Override
    public String toString() {
        return "Response[" + responseCode + ", " + responseBody + "]";
    }
}
