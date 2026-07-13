package com.example.dtos.profile;

public class TeacherDashboardDto {

    private double groupAverage;
    private int totalStudents;
    private int completedActivities;
    private int pendingActivities;

    public TeacherDashboardDto() {
    }

    public TeacherDashboardDto(
            double groupAverage,
            int totalStudents,
            int completedActivities,
            int pendingActivities
    ) {
        this.groupAverage = groupAverage;
        this.totalStudents = totalStudents;
        this.completedActivities = completedActivities;
        this.pendingActivities = pendingActivities;
    }

    public double getGroupAverage() {
        return groupAverage;
    }

    public void setGroupAverage(double groupAverage) {
        this.groupAverage = groupAverage;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(int totalStudents) {
        this.totalStudents = totalStudents;
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