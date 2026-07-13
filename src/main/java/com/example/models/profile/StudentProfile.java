package com.example.models.profile;

import java.math.BigDecimal;

public class StudentProfile {

    private String id;
    private String name;
    private String email;
    private String tuition;
    private BigDecimal totalPoints;

    public StudentProfile() {
    }

    public StudentProfile(
            String id,
            String name,
            String email,
            String tuition,
            BigDecimal totalPoints) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.tuition = tuition;
        this.totalPoints = totalPoints;
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

    public BigDecimal getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(BigDecimal totalPoints) {
        this.totalPoints = totalPoints;
    }
}