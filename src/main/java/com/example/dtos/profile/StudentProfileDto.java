package com.example.dtos.profile;

public class StudentProfileDto {

    private String id;
    private String name;
    private String email;
    private String tuition;

    public StudentProfileDto() {
    }

    public StudentProfileDto(
            String id,
            String name,
            String email,
            String tuition
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.tuition = tuition;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTuition() {
        return tuition;
    }

    public void setTuition(String tuition) {
        this.tuition = tuition;
    }
}