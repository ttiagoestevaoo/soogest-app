package http_responses;

public class ResponseAPI {

    private int responseCode;
    private String responseBody;

    public static int HTTP_OK = 200;
    public static int HTTP_UNAUTHORIZED = 401;
    public static int HTTP_BAD_REQUEST = 400;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }



}
