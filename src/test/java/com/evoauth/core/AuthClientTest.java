package com.evoauth.core;

import com.evoauth.models.User;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class AuthClientTest {
    private MockWebServer mockWebServer;
    private AuthClient authClient;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        AuthConfig config = new AuthConfig.Builder()
                .baseUrl(mockWebServer.url("/").toString())
                .apiKey("test-key")
                .build();

        authClient = new AuthClient(config);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void testLoginSuccess() {
        String jsonResponse = "{\n" +
                "  \"access_token\": \"eyJhb...\",\n" +
                "  \"refresh_token\": \"def456...\",\n" +
                "  \"expires_in\": 3600,\n" +
                "  \"user\": {\n" +
                "    \"id\": \"user_123\",\n" +
                "    \"email\": \"test@example.com\",\n" +
                "    \"roles\": [\"USER\"]\n" +
                "  }\n" +
                "}";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse));

        User user = authClient.login("test@example.com", "password");

        assertNotNull(user);
        assertEquals("user_123", user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertTrue(authClient.isAuthenticated());
        assertEquals("eyJhb...", authClient.getAccessToken());
    }

    @Test
    void testLoginFailure() {
        String errorResponse = "{\"code\": \"AUTH_FAILED\", \"message\": \"Invalid credentials\"}";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(401)
                .setBody(errorResponse));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            authClient.login("test@example.com", "wrong_pass");
        });

        assertTrue(exception.getMessage().contains("Invalid credentials"));
        assertFalse(authClient.isAuthenticated());
    }
}
