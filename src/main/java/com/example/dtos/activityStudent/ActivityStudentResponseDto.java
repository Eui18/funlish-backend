package com.example.dtos.activityStudent;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ActivityStudentResponseDto {

    private String id;
    private String activityId;
    private String studentId;
    private String status;
    private BigDecimal score;
    private BigDecimal bonusPoints;
    private Integer timeSpent;
    private LocalDateTime startDate;
    private LocalDateTime deliveryDate;

    public ActivityStudentResponseDto(
            String id,
            String activityId,
            String studentId,
            String status,
            BigDecimal score,
            BigDecimal bonusPoints,
            Integer timeSpent,
            LocalDateTime startDate,
            LocalDateTime deliveryDate
    ){

        this.id = id;
        this.activityId = activityId;
        this.studentId = studentId;
        this.status = status;
        this.score = score;
        this.bonusPoints = bonusPoints;
        this.timeSpent = timeSpent;
        this.startDate = startDate;
        this.deliveryDate = deliveryDate;
    }

    public String getId(){
        return id;
    }

    public String getActivityId(){
        return activityId;
    }

    public String getStudentId(){
        return studentId;
    }

    public String getStatus(){
        return status;
    }

    public BigDecimal getScore(){
        return score;
    }

    public BigDecimal getBonusPoints(){
        return bonusPoints;
    }

    public Integer getTimeSpent(){
        return timeSpent;
    }

    public LocalDateTime getStartDate(){
        return startDate;
    }

    public LocalDateTime getDeliveryDate(){
        return deliveryDate;
    }
}