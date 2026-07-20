package com.example.dtos.scramble;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateScrambleDto {

    @NotBlank(message = "El contenido del scramble es obligatorio.")
    @Size(
        max = 250,
        message = "El contenido no puede superar los 250 caracteres."
    )
    private String correctContent;


    public CreateScrambleDto() {
    }

    public String getCorrectContent() {
        return correctContent;
    }

    public void setCorrectContent(String correctContent) {
        this.correctContent = correctContent;
    }
}
