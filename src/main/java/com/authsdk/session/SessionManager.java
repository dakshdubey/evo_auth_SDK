package com.authsdk.session;

import com.authsdk.models.AuthResponse;
import com.authsdk.models.User;

public class SessionManager {
    private final TokenStore tokenStore;

    public SessionManager(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    public SessionManager() {
        this(new InMemoryTokenStore());
    }

    public void startSession(AuthResponse response) {
        tokenStore.saveTokens(response.getAccessToken(), response.getRefreshToken(), response.getUser());
    }

    public void updateAccessToken(String newAccessToken) {
        // Keeps the existing refresh token and user
        tokenStore.saveTokens(newAccessToken, tokenStore.getRefreshToken(), tokenStore.getUser());
    }

    public void endSession() {
        tokenStore.clear();
    }

    public String getAccessToken() {
        return tokenStore.getAccessToken();
    }

    public String getRefreshToken() {
        return tokenStore.getRefreshToken();
    }

    public User getCurrentUser() {
        return tokenStore.getUser();
    }

    public boolean isAuthenticated() {
        return getAccessToken() != null;
    }
}
