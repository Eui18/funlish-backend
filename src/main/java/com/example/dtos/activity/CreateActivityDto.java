package com.example.dtos.activity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateActivityDto {

   @NotBlank(message = "El título es obligatorio.")
    @Size(max = 150, message = "El título no puede superar los 150 caracteres.")
    private String title;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres.")
    private String description;

    @NotBlank(message = "El tipo de actividad es obligatorio.")
    private String type;

    @NotNull(message = "El puntaje máximo es obligatorio.")
    @Min(value = 1, message = "El puntaje máximo debe ser mayor a 0.")
    private Integer maxScore;

    @NotNull(message = "La duración es obligatoria.")
    @Min(value = 1, message = "La duración debe ser mayor a 0.")
    @Max(value = 180, message = "La duración no puede superar los 180 minutos.")
    private Integer durationMinutes;

    @NotBlank(message = "La fecha de inicio es obligatoria.")
    private String startDate;

    @NotBlank(message = "La fecha final es obligatoria.")
    private String endDate;

    @NotBlank(message = "La hora de inicio es obligatoria.")
    private String startTime;

    @NotBlank(message = "La hora final es obligatoria.")
    private String endTime;

    public CreateActivityDto() {
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