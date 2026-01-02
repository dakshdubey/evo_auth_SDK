package com.evoauth.session;

import com.evoauth.models.User;

public class InMemoryTokenStore implements TokenStore {
    private String accessToken;
    private String refreshToken;
    private User user;

    @Override
    public void saveTokens(String accessToken, String refreshToken, User user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.user = user;
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public String getRefreshToken() {
        return refreshToken;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void clear() {
        this.accessToken = null;
        this.refreshToken = null;
        this.user = null;
    }
}
