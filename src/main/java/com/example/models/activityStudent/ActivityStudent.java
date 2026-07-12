package com.example.models.activityStudent;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ActivityStudent {

    private String id;
    private String activityId;
    private String studentId;
    private ActivityStudentStatus status;
    private BigDecimal score;
    private BigDecimal bonusPoints;
    private Integer timeSpent;
    private LocalDateTime startDate;
    private LocalDateTime deliveryDate;
    private Integer lastQuestion;

    public ActivityStudent(
            String id,
            String activityId,
            String studentId,
            ActivityStudentStatus status,
            BigDecimal score,
            BigDecimal bonusPoints,
            Integer timeSpent,
            LocalDateTime startDate,
            LocalDateTime deliveryDate,
            Integer lastQuestion
    ) {
        this.id = id;
        this.activityId = activityId;
        this.studentId = studentId;
        this.status = status;
        this.score = score;
        this.bonusPoints = bonusPoints;
        this.timeSpent = timeSpent;
        this.startDate = startDate;
        this.deliveryDate = deliveryDate;
        this.lastQuestion = lastQuestion;
    }

    public String getId() {
        return id;
    }

    public String getActivityId() {
        return activityId;
    }

    public String getStudentId() {
        return studentId;
    }

    public ActivityStudentStatus getStatus() {
        return status;
    }

    public BigDecimal getScore() {
        return score;
    }

    public BigDecimal getBonusPoints() {
        return bonusPoints;
    }

    public Integer getTimeSpent() {
        return timeSpent;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }


    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public Integer getLastQuestion() {
        return lastQuestion;
    }

    public void setStatus(ActivityStudentStatus status) {
        this.status = status;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public void setBonusPoints(BigDecimal bonusPoints) {
        this.bonusPoints = bonusPoints;
    }

    public void setTimeSpent(Integer timeSpent) {
        this.timeSpent = timeSpent;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void setLastQuestion(Integer lastQuestion) {
        this.lastQuestion = lastQuestion;
    }
}