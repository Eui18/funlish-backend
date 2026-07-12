package com.example.dtos.activity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class UpdateActivityDto {

    @Size(max = 150, message = "El título no puede superar los 150 caracteres.")
    private String title;

    @Size(max =500, message = "La descripción no puede superar los 500 caracteres.")
    private String description;

    private String type;

    @Min(value = 1, message = "La duración debe ser mayor a 0 minutos.")
    private Integer durationMinutes;

    @Min(value = 1, message = "El puntaje debe ser mayor a 0.")
    private Integer scorePerQuestion;

    public UpdateActivityDto() {
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
}