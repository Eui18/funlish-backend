package com.example.dtos.activity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class UpdateActivityDto {

   @Size(max = 150, message = "El título no puede superar los 150 caracteres.")
    private String title;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres.")
    private String description;

    private String type;

    @Min(value = 1, message = "El puntaje máximo debe ser mayor a 0")
    private Integer maxScore;

    @Min(value = 1, message = "La duración debe ser mayor a 0.")
    @Max(value = 180, message = "La duración no puede superar los 180 minutos.")
    private Integer durationMinutes;

    private String startDate;

    private String endDate;

    private String startTime;

    private String endTime;

    public UpdateActivityDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}