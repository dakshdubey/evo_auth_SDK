package com.authsdk.models;

public class MfaVerifyRequest {
    private String code;

    public MfaVerifyRequest(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
