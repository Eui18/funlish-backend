package com.example.models.activity;

import java.time.LocalDateTime;

public class Activity {

    private String id;
    private String topicId;
    private String teacherId;
    private String title;
    private String description;
    private ActivityType type;
    private int durationMinutes;
    private int scorePerQuestion;
    private ActivityStatus status;
    private LocalDateTime createdAt;

    public Activity(
            String id,
            String topicId,
            String teacherId,
            String title,
            String description,
            ActivityType type,
            int durationMinutes,
            int scorePerQuestion,
            ActivityStatus status,
            LocalDateTime createdAt) {

        this.id = id;
        this.topicId = topicId;
        this.teacherId = teacherId;
        this.title = title;
        this.description = description;
        this.type = type;
        this.durationMinutes = durationMinutes;
        this.scorePerQuestion = scorePerQuestion;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getTopicId() {
        return topicId;
    }

    public String getTeacherId() {
        return teacherId;
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
}