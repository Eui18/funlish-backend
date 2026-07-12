package com.example.dtos.activity;

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

    @NotBlank(message = "El tema es obligatorio.")
    private String topicId;

    @NotNull(message = "La duración es obligatoria.")
    @Min(value = 1, message = "La duración debe ser mayor a 0 minutos.")
    private Integer durationMinutes;

    @NotNull(message = "El puntaje por pregunta es obligatorio.")
    @Min(value = 1, message = "El puntaje debe ser mayor a 0.")
    private Integer scorePerQuestion;

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

    public String getTopicId() {
        return topicId;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public Integer getScorePerQuestion() {
        return scorePerQuestion;
    }
}