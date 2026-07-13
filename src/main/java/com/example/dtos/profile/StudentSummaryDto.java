package com.example.dtos.profile;

import java.math.BigDecimal;

public class StudentSummaryDto {

    private String id;
    private String name;
    private String tuition;
    private BigDecimal points;
    private int position;

    public StudentSummaryDto() {
    }

    public StudentSummaryDto(
            String id,
            String name,
            String tuition,
            BigDecimal points,
            int position
    ) {
        this.id = id;
        this.name = name;
        this.tuition = tuition;
        this.points = points;
        this.position = position;
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

    public String getTuition() {
        return tuition;
    }

    public void setTuition(String tuition) {
        this.tuition = tuition;
    }

    public BigDecimal getPoints() {
        return points;
    }

    public void setPoints(BigDecimal points) {
        this.points = points;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}