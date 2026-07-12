package com.example.dtos.scramble;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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
}