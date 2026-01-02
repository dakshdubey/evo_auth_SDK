# EvoAuth 

[![Build Status](https://github.com/dakshdubey/evo-auth-sdk/actions/workflows/publish.yml/badge.svg)](https://github.com/dakshdubey/evo-auth-sdk/actions)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

**EvoAuth** is a premium Java SDK for modern authentication. It handles the "boring" stuff (JWTs, Refresh Tokens, 2FA, OAuth2) so you can build your product faster.

##  Features

-  **Elegant API**: Simple, fluent interfaces for developers.
-  **Auto-Refresh**: Intelligent interceptors handle token expiry automatically.
-  **MFA Ready**: Native support for TOTP (Google Authenticator).
-  **Social Login**: Built-in OAuth2 triggers for Google, GitHub, and more.
-  **Spring Boot Native**: Auto-configuration out of the box.

##  Installation

### Maven Central
```xml
<dependency>
    <groupId>io.github.dakshdubey</groupId>
    <artifactId>evo-auth-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

### JitPack (Immediate Use)
If you want to use the latest version directly from GitHub:

1. Add the repository to your `pom.xml`:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

2. Add the dependency:
```xml
<dependency>
    <groupId>com.github.dakshdubey</groupId>
    <artifactId>evo-auth-sdk</artifactId>
    <version>v1.0.0</version>
</dependency>
```

##  Quick Start

```java
AuthClient auth = new AuthClient(new AuthConfig.Builder()
    .baseUrl("https://api.evoauth.com")
    .apiKey("your-key")
    .build());

User user = auth.login("user@example.com", "password");
System.out.println("Welcome, " + user.getFirstName());
```

##  Documentation

For detailed guides, API reference, and Spring Boot integration, check out our **[Full Documentation](file:///e:/JavaAuthSDK/DOCUMENTATION.md)**.

## Building from Source

```bash
git clone https://github.com/dakshdubey/evo-auth-sdk.git
cd evo-auth-sdk
mvn clean install
```

##  Contributing

Contributions are welcome! Please read our contributing guidelines for details.

---

Built with  by the EvoXploit Team Owned by Daksha Dubey
