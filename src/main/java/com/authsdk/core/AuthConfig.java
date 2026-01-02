package com.authsdk.core;

public class AuthConfig {
    private final String baseUrl;
    private final String apiKey;
    private final long connectionTimeoutMs;

    private AuthConfig(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.apiKey = builder.apiKey;
        this.connectionTimeoutMs = builder.connectionTimeoutMs;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public long getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public static class Builder {
        private String baseUrl;
        private String apiKey;
        private long connectionTimeoutMs = 10000; // Default 10s

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder connectionTimeoutMs(long connectionTimeoutMs) {
            this.connectionTimeoutMs = connectionTimeoutMs;
            return this;
        }

        public AuthConfig build() {
            if (baseUrl == null || baseUrl.isBlank()) {
                throw new IllegalArgumentException("Base URL must not be empty");
            }
            // Normalize URL to not have trailing slash
            if (baseUrl.endsWith("/")) {
                this.baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
            }
            return new AuthConfig(this);
        }
    }
}
