package com.authsdk.session;

import com.authsdk.models.User;

public interface TokenStore {
    void saveTokens(String accessToken, String refreshToken, User user);
    String getAccessToken();
    String getRefreshToken();
    User getUser();
    void clear();
}
