package com.evoauth.demo;

import com.evoauth.core.AuthConfig;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;

@SpringBootApplication
@ComponentScan(basePackages = { "com.evoauth" })
public class DemoApplication {

    private MockWebServer mockAuthServer;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @PostConstruct
    public void startMockServer() throws IOException {
        mockAuthServer = new MockWebServer();
        mockAuthServer.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                String path = request.getPath();
                if (path.equals("/api/v1/auth/login")) {
                    return new MockResponse().setResponseCode(200).setBody("{\n" +
                            "  \"access_token\": \"mock_access_token_xyz\",\n" +
                            "  \"refresh_token\": \"mock_refresh_token_123\",\n" +
                            "  \"expires_in\": 3600,\n" +
                            "  \"user\": {\n" +
                            "    \"id\": \"user_1\",\n" +
                            "    \"email\": \"demo@evoauth.com\",\n" +
                            "    \"firstName\": \"Evo\",\n" +
                            "    \"lastName\": \"User\",\n" +
                            "    \"roles\": [\"ADMIN\", \"USER\"]\n" +
                            "  }\n" +
                            "}");
                }
                if (path.equals("/api/v1/2fa/enable")) {
                    return new MockResponse().setResponseCode(200).setBody("{\n" +
                            "  \"secret\": \"JBSWY3DPEHPK3PXP\",\n" +
                            "  \"qrCodeUrl\": \"https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=otpauth://totp/EvoAuthDemo?secret=JBSWY3DPEHPK3PXP\"\n"
                            +
                            "}");
                }
                return new MockResponse().setResponseCode(404);
            }
        });
        mockAuthServer.start(9090);
        System.out.println("🚀 Mock Auth Server started on port 9090");
    }

    @PreDestroy
    public void stopMockServer() throws IOException {
        if (mockAuthServer != null) {
            mockAuthServer.shutdown();
        }
    }

    @Bean
    public AuthConfig authConfig() {
        return new AuthConfig.Builder()
                .baseUrl("http://localhost:9090")
                .apiKey("demo-api-key")
                .build();
    }
}
