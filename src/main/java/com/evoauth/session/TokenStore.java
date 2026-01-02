package com.evoauth.session;

import com.evoauth.models.User;

public interface TokenStore {
    void saveTokens(String accessToken, String refreshToken, User user);

    String getAccessToken();

    String getRefreshToken();

    User getUser();

    void clear();
}
