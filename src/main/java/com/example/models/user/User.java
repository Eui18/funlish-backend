package com.example.models.user;

public class User {

    private String id;
    private String name;
    private String tuition;
    private String email;
    private String password;
    private Role role;
    private String groupId;

    public User() {
    }

    public User(
            String id,
            String name,
            String tuition,
            String email,
            String password,
            Role role) {

        this.id = id;
        this.name = name;
        this.tuition = tuition;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(
            String id,
            String name,
            String tuition,
            String email,
            String password,
            Role role,
            String groupId) {

        this.id = id;
        this.name = name;
        this.tuition = tuition;
        this.email = email;
        this.password = password;
        this.role = role;
        this.groupId = groupId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTuition() {
        return this.tuition;
    }

    public void setTuition(String tuition) {
        this.tuition = tuition;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}