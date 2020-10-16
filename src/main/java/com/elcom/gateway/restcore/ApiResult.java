package com.elcom.gateway.restcore;

public class ApiResult {

    private int code;
    private boolean status;
    private String bodyContent;

    public boolean getStatus() {
        return code >= 200 && code < 300;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getBodyContent() {
        return bodyContent;
    }

    public void setBodyContent(String bodyContent) {
        this.bodyContent = bodyContent;
    }
}
