package com.example.dtos.users;

import com.example.models.user.Role;

public class UserResponseDto {
    private String id;
    private String name;
    private String tuition;
    private String email;
    private Role role;

    public UserResponseDto(String id, String name, String tuition, String email, Role role) {
        this.id = id;
        this.name = name;
        this.tuition = tuition;
        this.email = email;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public String getTuition() {
        return this.tuition;
    }

    public String getEmail() {
        return this.email;
    }


    public Role getRole() {
        return this.role;
    }

}
