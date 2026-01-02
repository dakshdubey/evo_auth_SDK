package com.evoauth.models;

public enum OAuthProvider {
    GOOGLE("google"),
    GITHUB("github"),
    MICROSOFT("microsoft");

    private final String providerName;

    OAuthProvider(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderName() {
        return providerName;
    }
}
