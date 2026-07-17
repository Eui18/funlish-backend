package com.example.dtos.activity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateActivityDto {

    @NotBlank(message = "El título es obligatorio.")
    @Size(max = 150)
    private String title;

    @Size(max = 500)
    private String description;

    @NotBlank(message = "El tipo de actividad es obligatorio.")
    private String type;

    @NotNull(message = "La duración es obligatoria.")
    @Min(value = 1)
    private Integer durationMinutes;

    @NotNull(message = "El puntaje por pregunta es obligatorio.")
    @Min(value = 1)
    private Integer scorePerQuestion;


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


    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public void setScorePerQuestion(Integer scorePerQuestion) {
        this.scorePerQuestion = scorePerQuestion;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}