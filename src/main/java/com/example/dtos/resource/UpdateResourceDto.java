package com.example.dtos.resource;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateResourceDto {

    @NotBlank(message = "El nombre es obligatorio para actualizar.")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres.")
    private String name;

    @NotBlank(message = "El tipo es obligatorio.")
    private String type;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres.")
    private String description;

    @NotBlank(message = "La URL es obligatoria.")
    @URL(message = "La URL debe ser un enlace válido.")
    private String url;

    public UpdateResourceDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}