package http_requests;

import java.util.HashMap;

public class HttpCall {
    public static final int GET = 1 ;
    public static final int POST = 2;
    public static final int PUT = 3;
    public static final int DELETE = 4;

    public String token = "";
    private String url;
    private int methodType;
    private HashMap<String,String> params;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getMethodType() {
        return methodType;
    }

    public void setMethodType(int methodType) {
        this.methodType = methodType;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

}
