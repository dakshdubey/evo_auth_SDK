package com.evoauth.examples;

import com.evoauth.core.AuthClient;
import com.evoauth.core.AuthConfig;
import com.evoauth.exceptions.AuthApiException;
import com.evoauth.models.User;
import com.evoauth.security.PermissionGuard;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * A simulated "Banking Application" that demonstrates how a developer
 * would use the Java Auth SDK in a real scenario.
 */
public class BankAppDemo {

    @Test
    public void runBankingDemo() throws IOException {
        MockWebServer authServer = new MockWebServer();
        authServer.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                String path = request.getPath();
                System.out.println("  [Server] Received: " + request.getMethod() + " " + path);

                if (path.equals("/api/v1/auth/login")) {
                    String body = request.getBody().readUtf8();
                    if (body.contains("admin@bank.com")) {
                        return new MockResponse().setResponseCode(200).setBody("{\n" +
                                "  \"access_token\": \"admin_token_123\",\n" +
                                "  \"refresh_token\": \"refresh_xyz\",\n" +
                                "  \"user\": { \"id\": \"1\", \"email\": \"admin@bank.com\", \"firstName\": \"Admin\", \"roles\": [\"ADMIN\", \"USER\"] }\n"
                                +
                                "}");
                    } else if (body.contains("user@bank.com")) {
                        return new MockResponse().setResponseCode(200).setBody("{\n" +
                                "  \"access_token\": \"user_token_456\",\n" +
                                "  \"refresh_token\": \"refresh_abc\",\n" +
                                "  \"user\": { \"id\": \"2\", \"email\": \"user@bank.com\", \"firstName\": \"Alice\", \"roles\": [\"USER\"] }\n"
                                +
                                "}");
                    } else {
                        return new MockResponse().setResponseCode(401)
                                .setBody("{\"message\": \"Invalid credentials\"}");
                    }
                }

                if (path.equals("/api/v1/2fa/enable")) {
                    return new MockResponse().setResponseCode(200).setBody("{\n" +
                            "  \"secret\": \"JBSWY3DPEHPK3PXP\",\n" +
                            "  \"qrCodeUrl\": \"https://chart.googleapis.com/chart?chs=200x200&cht=qr&chl=otpauth://totp/BankApp:admin@bank.com?secret=JBSWY3DPEHPK3PXP\",\n"
                            +
                            "  \"backupCodes\": [\"111111\", \"222222\"]\n" +
                            "}");
                }

                return new MockResponse().setResponseCode(404);
            }
        });
        authServer.start();

        System.out.println("\n=== STARTING BANK APP DEMO (EvoAuth) ===\n");

        AuthConfig config = new AuthConfig.Builder()
                .baseUrl(authServer.url("").toString())
                .apiKey("bank_api_key_v1")
                .build();

        AuthClient auth = new AuthClient(config);

        System.out.println("--- Scenario A: Regular User Login ---");
        try {
            User alice = auth.login("user@bank.com", "password123");
            System.out.println("✅ Login Successful! Welcome, " + alice.getFirstName());

            System.out.println("Attempting to access ADMIN Vault...");
            if (PermissionGuard.hasRole(alice, "ADMIN")) {
                System.out.println("🔓 Vault Unlocked!");
            } else {
                System.out.println("❌ Access Denied: You do not have ADMIN permissions.");
            }
        } catch (AuthApiException e) {
            System.err.println("Login Failed: " + e.getMessage());
        }

        System.out.println("\n--- Scenario B: Admin Login ---");
        try {
            User admin = auth.login("admin@bank.com", "superSecurePass");
            System.out.println("✅ Login Successful! Welcome, " + admin.getFirstName());

            System.out.println("Attempting to access ADMIN Vault...");
            if (PermissionGuard.hasRole(admin, "ADMIN")) {
                System.out.println("🔓 Vault Unlocked! Accessing sensitive gold reserves...");

                System.out.println("\n[Security Policy] Admins must enable 2FA.");
                var mfaResponse = auth.enableMfa();
                System.out.println("📲 2FA Setup Initiated.");
                System.out.println("   Secret Key: " + mfaResponse.getSecret());
                System.out.println("   QR Code: " + mfaResponse.getQrCodeUrl());
            } else {
                System.out.println("❌ Access Denied.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        authServer.shutdown();
        System.out.println("\n=== DEMO COMPLETED ===");
    }
}
