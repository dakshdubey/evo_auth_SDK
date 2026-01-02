package com.authsdk.security;

import com.authsdk.models.User;

import java.util.Arrays;
import java.util.List;

public class PermissionGuard {

    /**
     * Checks if the user has the specified role.
     */
    public static boolean hasRole(User user, String role) {
        if (user == null || user.getRoles() == null) {
            return false;
        }
        return user.getRoles().contains(role);
    }

    /**
     * Checks if the user has ANY of the specified roles.
     */
    public static boolean hasAnyRole(User user, String... roles) {
        if (user == null || user.getRoles() == null) {
            return false;
        }
        List<String> userRoles = user.getRoles();
        for (String role : roles) {
            if (userRoles.contains(role)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the user has ALL of the specified roles.
     */
    public static boolean hasAllRoles(User user, String... roles) {
        if (user == null || user.getRoles() == null) {
            return false;
        }
        List<String> userRoles = user.getRoles();
        return userRoles.containsAll(Arrays.asList(roles));
    }
}
