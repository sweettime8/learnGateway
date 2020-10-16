package com.elcom.gateway.restcore;

public class ISRestCoreException extends Exception {

    private static final long serialVersionUID = -3373951972123927805L;

    public int statusCode = 500;

    public ISRestCoreException(String message) {
        super(message);
    }
}
