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


    public CreateActivityDto(){}


    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public String getType(){
        return type;
    }

    public Integer getDurationMinutes(){
        return durationMinutes;
    }

    public Integer getScorePerQuestion(){
        return scorePerQuestion;
    }
}