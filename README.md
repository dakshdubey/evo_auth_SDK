# EvoAuth 

[![Build Status](https://github.com/dakshdubey/evo-auth-sdk/actions/workflows/publish.yml/badge.svg)](https://github.com/dakshdubey/evo-auth-sdk/actions)
[![License: Apache 2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

**EvoAuth** is a premium Java SDK for modern authentication. It handles the "boring" stuff (JWTs, Refresh Tokens, 2FA, OAuth2) so you can build your product faster.

##  Features

-  **Elegant API**: Simple, fluent interfaces for developers.
-  **Auto-Refresh**: Intelligent interceptors handle token expiry automatically.
-  **MFA Ready**: Native support for TOTP (Google Authenticator).
-  **Social Login**: Built-in OAuth2 triggers for Google, GitHub, and more.
-  **Spring Boot Native**: Auto-configuration out of the box.

## 🚀 Installation

### Option 1: Maven Central (Recommended)
Add this to your project's `pom.xml`:

```xml
<dependency>
    <groupId>io.github.dakshdubey</groupId>
    <artifactId>evo-auth-sdk</artifactId>
    <version>1.0.6</version>
</dependency>
```

### Option 2: JitPack (Latest Version)
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
    <version>v1.0.6</version> 
</dependency>
```

## 🛠️ Quick Setup & Usage

To use EvoAuth in your Java project, follow these simple steps:

### 1. Import the SDK
Just like you import `java.util.Scanner`, you need to import the EvoAuth components:

```java
import com.evoauth.core.AuthClient;
import com.evoauth.core.AuthConfig;
import com.evoauth.models.User;
```

### 2. Initialize and Login
```java
public class MyApp {
    public static void main(String[] args) {
        // 1. Setup Configuration
        AuthConfig config = new AuthConfig.Builder()
            .baseUrl("https://api.evoauth.com")
            .apiKey("your-api-key-here")
            .build();

        // 2. Create the Client
        AuthClient auth = new AuthClient(config);

        // 3. User Login
        try {
            User user = auth.login("john@example.com", "password123");
            System.out.println("Login Success! Welcome " + user.getFirstName());
        } catch (Exception e) {
            System.err.println("Login failed: " + e.getMessage());
        }
    }
}
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
