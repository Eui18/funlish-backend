package com.example.dtos.activity;

public class ActivityResponseDto {

    private String id;
    private String topicId;
    private String title;
    private String description;
    private String type;
    private Integer durationMinutes;
    private Integer scorePerQuestion;
    private String status;

    public ActivityResponseDto(
            String id,
            String topicId,
            String title,
            String description,
            String type,
            Integer durationMinutes,
            Integer scorePerQuestion,
            String status) {

        this.id = id;
        this.topicId = topicId;
        this.title = title;
        this.description = description;
        this.type = type;
        this.durationMinutes = durationMinutes;
        this.scorePerQuestion = scorePerQuestion;
        this.status = status;
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

    public String getType() {
        return type;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public Integer getScorePerQuestion() {
        return scorePerQuestion;
    }

    public String getStatus() {
        return status;
    }
}