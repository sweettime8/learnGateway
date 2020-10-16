package com.elcom.gateway.restcore;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    HEAD("HEAD"),
    OPTIONS("OPTIONS"),
    PUT("PUT"),
    DELETE("DELETE"),
    TRACE("TRACE");

    String _value;

    HttpMethod(String p) {
        _value = p;
    }

    public String getDescription() {
        return _value;
    }

}
