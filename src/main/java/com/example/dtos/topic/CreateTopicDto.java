package com.example.dtos.topic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateTopicDto {

    @NotBlank(message = "El título es obligatorio.")
    @Size(min = 3, max = 150, message = "El título debe tener entre 3 y 150 caracteres.")
    private String title;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres.")
    private String description;

    public CreateTopicDto() {
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
}