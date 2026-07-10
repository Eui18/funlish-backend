package com.example.dtos.unit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateUnitDto {

    @NotBlank(message = "El nombre de la unidad es obligatorio.")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres.")
    private String name;

    public UpdateUnitDto() {
    }

    public UpdateUnitDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}