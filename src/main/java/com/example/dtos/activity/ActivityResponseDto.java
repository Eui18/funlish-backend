package com.example.dtos.activity;

public class ActivityResponseDto {

    private String id;
    private String title;
    private String description;
    private String type;
    private int maxScore;
    private int durationMinutes;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;

    public ActivityResponseDto() {
    }

    public ActivityResponseDto(
            String id,
            String title,
            String description,
            String type,
            int maxScore,
            int durationMinutes,
            String startDate,
            String endDate,
            String startTime,
            String endTime) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.maxScore = maxScore;
        this.durationMinutes = durationMinutes;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}