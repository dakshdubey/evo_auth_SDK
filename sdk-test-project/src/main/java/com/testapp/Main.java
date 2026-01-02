package com.testapp;

import com.evoauth.core.AuthClient;
import com.evoauth.core.AuthConfig;
import com.evoauth.models.User;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== EvoAuth SDK Test Project ===");

        // 1. Setup Configuration
        AuthConfig config = new AuthConfig.Builder()
                .baseUrl("https://api.evoauth.com")
                .apiKey("demo-api-key-12345")
                .connectionTimeoutMs(5000)
                .build();

        // 2. Initialize the Client
        AuthClient auth = new AuthClient(config);

        System.out.println("SDK initialized successfully!");
        System.out.println("Imports used: AuthClient, AuthConfig, User");

        // Note: Real login would require a valid server, but the objects are ready to
        // use.
        System.out.println("Project setup is complete and ready for development.");
    }
}
