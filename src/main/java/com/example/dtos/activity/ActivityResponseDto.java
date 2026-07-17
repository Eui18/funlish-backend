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

    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;


    public ActivityResponseDto(
            String id,
            String topicId,
            String title,
            String description,
            String type,
            Integer durationMinutes,
            Integer scorePerQuestion,
            String status,
            String startDate,
            String endDate,
            String startTime,
            String endTime) {

        this.id = id;
        this.topicId = topicId;
        this.title = title;
        this.description = description;
        this.type = type;
        this.durationMinutes = durationMinutes;
        this.scorePerQuestion = scorePerQuestion;
        this.status = status;

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