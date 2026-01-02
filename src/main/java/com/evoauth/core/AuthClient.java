package com.evoauth.core;

import com.evoauth.exceptions.AuthApiException;
import com.evoauth.exceptions.AuthSdkException;
import com.evoauth.http.HttpClientProvider;
import com.evoauth.models.*;
import com.evoauth.session.SessionManager;

public class AuthClient {
    private final HttpClientProvider httpClient;
    private final SessionManager sessionManager;

    public AuthClient(AuthConfig config) {
        this(config, new SessionManager());
    }

    // Exposed for testing dependency injection
    public AuthClient(AuthConfig config, SessionManager sessionManager) {
        this.httpClient = new HttpClientProvider(config);
        this.sessionManager = sessionManager;

        // Configure Auto-Refresh
        this.httpClient.setTokenRefresher(() -> {
            try {
                refreshSession(); // Synchronous call
                return sessionManager.getAccessToken();
            } catch (Exception e) {
                return null;
            }
        });
    }

    /**
     * Authenticates a user and starts a session.
     */
    public User login(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);
        AuthResponse response = httpClient.post("/api/v1/auth/login", request, AuthResponse.class);

        if (response != null) {
            sessionManager.startSession(response);
            return response.getUser();
        }
        throw new AuthSdkException("Login failed: Empty response");
    }

    /**
     * Registers a new user.
     */
    public User signup(String email, String password, String firstName, String lastName) {
        SignupRequest request = new SignupRequest(email, password, firstName, lastName);
        // Assuming signup also returns AuthResponse (auto-login), or just User if not.
        // Let's assume it returns AuthResponse for better UX.
        AuthResponse response = httpClient.post("/api/v1/auth/signup", request, AuthResponse.class);

        if (response != null) {
            sessionManager.startSession(response);
            return response.getUser();
        }
        throw new AuthSdkException("Signup failed: Empty response");
    }

    /**
     * Logs out the user and clears the session.
     */
    public void logout() {
        if (!sessionManager.isAuthenticated())
            return;

        try {
            // Best effort logout on server
            httpClient.post("/api/v1/auth/logout", null, Void.class, sessionManager.getAccessToken());
        } catch (Exception ignored) {
            // Log warning but proceed to clear local session
        } finally {
            sessionManager.endSession();
        }
    }

    /**
     * Refreshes the access token using the refresh token.
     */
    public void refreshSession() {
        String refreshToken = sessionManager.getRefreshToken();
        if (refreshToken == null) {
            throw new AuthSdkException("No refresh token available");
        }

        RefreshRequest request = new RefreshRequest(refreshToken);
        try {
            AuthResponse response = httpClient.post("/api/v1/auth/refresh", request, AuthResponse.class);
            if (response != null) {
                // If the server returns a new refresh token, update it. If not, keep the old
                // one (logic depends on server).
                // Assuming server returns full AuthResponse
                sessionManager.startSession(response);
            }
        } catch (AuthApiException e) {
            // If refresh fails (e.g., token expired), clear session
            if (e.getStatusCode() == 401 || e.getStatusCode() == 403) {
                sessionManager.endSession();
            }
            throw e;
        }
    }

    public boolean isAuthenticated() {
        return sessionManager.isAuthenticated();
    }

    public User getCurrentUser() {
        return sessionManager.getCurrentUser();
    }

    public String getAccessToken() {
        return sessionManager.getAccessToken();
    }

    /**
     * Initiates 2FA enrollment. Returns the secret key and QR code URL.
     */
    public MfaResponse enableMfa() {
        if (!sessionManager.isAuthenticated()) {
            throw new AuthSdkException("User must be logged in to enable MFA");
        }
        return httpClient.post("/api/v1/2fa/enable", null, MfaResponse.class, sessionManager.getAccessToken());
    }

    /**
     * Verifies the 2FA code to finalize enrollment or login.
     */
    public boolean verifyMfa(String code) {
        if (!sessionManager.isAuthenticated()) {
            throw new AuthSdkException("User must be logged in to verify MFA");
        }
        MfaVerifyRequest request = new MfaVerifyRequest(code);
        try {
            // Assuming 200 OK means verified
            httpClient.post("/api/v1/2fa/verify", request, Void.class, sessionManager.getAccessToken());
            return true;
        } catch (AuthApiException e) {
            return false;
        }
    }

    /**
     * Generates the URL to redirect the user to for Social Login.
     */
    public String getSocialLoginUrl(OAuthProvider provider, String callbackUrl) {
        // In a real implementation, this might call the backend to get a signed URL
        // or construct it locally if the formula is known.
        // Assuming backend handles the state generation for simplicity.
        // GET /api/v1/oauth/url?provider=google&callback=...
        // For now, let's construct it assuming standard format:
        return httpClient.get(
                "/api/v1/oauth/authorize?provider=" + provider.getProviderName() + "&redirect_uri=" + callbackUrl,
                String.class);
    }

    /**
     * Exchanges the OAuth callback code for a User session.
     */
    public User handleSocialCallback(String tempAuthCode) {
        // POST /api/v1/oauth/callback { "code": "..." }
        // Returns standard AuthResponse
        // We'll reuse a generic Map or DTO here. Let's create an inner class or use
        // Map.
        class CodeRequest {
            @SuppressWarnings("unused")
            public String code;

            public CodeRequest(String c) {
                this.code = c;
            }
        }

        AuthResponse response = httpClient.post("/api/v1/oauth/callback", new CodeRequest(tempAuthCode),
                AuthResponse.class);
        if (response != null) {
            sessionManager.startSession(response);
            return response.getUser();
        }
        throw new AuthSdkException("OAuth callback failed");
    }
}
