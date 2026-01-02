package com.evoauth.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RefreshRequest {
    @JsonProperty("refresh_token")
    private String refreshToken;

    public RefreshRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
