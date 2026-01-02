package com.authsdk.exceptions;

public class AuthSdkException extends RuntimeException {
    public AuthSdkException(String message) {
        super(message);
    }

    public AuthSdkException(String message, Throwable cause) {
        super(message, cause);
    }
}
