package com.evoauth.exceptions;

public class AuthApiException extends AuthSdkException {
    private final int statusCode;

    public AuthApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
