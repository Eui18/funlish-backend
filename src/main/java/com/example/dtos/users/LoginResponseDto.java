package com.example.dtos.users;

import com.example.models.user.Role;

public class LoginResponseDto {

    private String id;
    private String name;
    private String tuition;
    private String email;
    private Role role;
    private String token;

    public LoginResponseDto(
            String id,
            String name,
            String tuition,
            String email,
            Role role,
            String token
    ) {
        this.id = id;
        this.name = name;
        this.tuition = tuition;
        this.email = email;
        this.role = role;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTuition() {
        return tuition;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public String getToken() {
        return token;
    }
}