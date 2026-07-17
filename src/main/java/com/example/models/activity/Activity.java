package com.example.models.activity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Activity {

    private String id;
    private String topicId;
    private String title;
    private String description;
    private ActivityType type;
    private int durationMinutes;
    private int scorePerQuestion;
    private ActivityStatus status;
    private LocalDateTime createdAt;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;


    public Activity(
            String id,
            String topicId,
            String title,
            String description,
            ActivityType type,
            int durationMinutes,
            int scorePerQuestion,
            ActivityStatus status,
            LocalDateTime createdAt,
            LocalDate startDate,
            LocalDate endDate,
            LocalTime startTime,
            LocalTime endTime) {

        this.id = id;
        this.topicId = topicId;
        this.title = title;
        this.description = description;
        this.type = type;
        this.durationMinutes = durationMinutes;
        this.scorePerQuestion = scorePerQuestion;
        this.status = status;
        this.createdAt = createdAt;

        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }


    public String getId() {
        return id;
    }

    public String getTopicId() {
        return topicId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ActivityType getType() {
        return type;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public int getScorePerQuestion() {
        return scorePerQuestion;
    }

    public ActivityStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public void setScorePerQuestion(int scorePerQuestion) {
        this.scorePerQuestion = scorePerQuestion;
    }

    public void setStatus(ActivityStatus status) {
        this.status = status;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}