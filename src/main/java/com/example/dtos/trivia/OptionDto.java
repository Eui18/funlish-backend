package com.example.dtos.trivia;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OptionDto {

    @NotBlank(message = "La opción no puede estar vacía.")
    private String text;

    @NotNull(message = "Debe indicar si la opción es correcta.")
    private Boolean correct;

    public OptionDto() {
    }

    public String getText() {
        return text;
    }

    public Boolean getCorrect() {
        return correct;
    }
}