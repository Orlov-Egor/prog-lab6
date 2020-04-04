package common.interaction;

import java.io.Serializable;

/**
 * Response code '0' - OK
 * Response code '1' - Warning
 * Response code '2' - Error
 */

// TODO: Подумать над тем, чтобы сделать responseCode перечислением

public class Response implements Serializable {
    private int responseCode;
    private String responseBody;

    public Response(int responseCode, String responseBody) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
    }

    @Override
    public String toString() {
        return "Response[" + responseCode + ", " + responseBody + "]";
    }
}
