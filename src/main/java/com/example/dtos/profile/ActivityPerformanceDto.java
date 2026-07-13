package com.example.dtos.profile;

public class ActivityPerformanceDto {

    private String activity;
    private String type;
    private double percentage;


    public ActivityPerformanceDto() {
    }

    public ActivityPerformanceDto(
            String activity,
            String type,
            double percentage
    ) {
        this.activity = activity;
        this.type = type;
        this.percentage = percentage;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}