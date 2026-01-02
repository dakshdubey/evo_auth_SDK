package com.evoauth.demo;

import com.evoauth.core.AuthClient;
import com.evoauth.models.MfaResponse;
import com.evoauth.models.User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/demo")
@CrossOrigin(origins = "*")
public class DemoController {

    private final AuthClient authClient;

    public DemoController(AuthClient authClient) {
        this.authClient = authClient;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> credentials) {
        try {
            User user = authClient.login(credentials.get("email"), credentials.get("password"));
            return Map.of(
                    "success", true,
                    "user", user,
                    "token", authClient.getAccessToken());
        } catch (Exception e) {
            return Map.of("success", false, "message", e.getMessage());
        }
    }

    @GetMapping("/me")
    public Map<String, Object> getCurrentUser() {
        if (authClient.isAuthenticated()) {
            return Map.of("success", true, "user", authClient.getCurrentUser());
        }
        return Map.of("success", false, "message", "Not authenticated");
    }

    @PostMapping("/2fa/enable")
    public Map<String, Object> enableMfa() {
        try {
            MfaResponse mfa = authClient.enableMfa();
            return Map.of("success", true, "mfa", mfa);
        } catch (Exception e) {
            return Map.of("success", false, "message", e.getMessage());
        }
    }

    @PostMapping("/logout")
    public Map<String, Object> logout() {
        authClient.logout();
        return Map.of("success", true);
    }
}
