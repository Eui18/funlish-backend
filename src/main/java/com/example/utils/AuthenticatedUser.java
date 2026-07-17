package com.example.utils;

public class AuthenticatedUser {

    private final String userId;
    private final String role;

    public AuthenticatedUser(String userId, String role) {
        this.userId = userId;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }
}