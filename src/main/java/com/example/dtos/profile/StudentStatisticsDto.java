package com.example.dtos.profile;

import java.math.BigDecimal;

public class StudentStatisticsDto {

    private BigDecimal totalPoints;
    private int completedActivities;
    private int pendingActivities;

    public StudentStatisticsDto() {
    }

    public StudentStatisticsDto(
            BigDecimal totalPoints,
            int completedActivities,
            int pendingActivities
    ) {
        this.totalPoints = totalPoints;
        this.completedActivities = completedActivities;
        this.pendingActivities = pendingActivities;
    }

    public BigDecimal getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(BigDecimal totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getCompletedActivities() {
        return completedActivities;
    }

    public void setCompletedActivities(int completedActivities) {
        this.completedActivities = completedActivities;
    }

    public int getPendingActivities() {
        return pendingActivities;
    }

    public void setPendingActivities(int pendingActivities) {
        this.pendingActivities = pendingActivities;
    }
}