package com.example.dtos.activityStudent;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AnswerDto {

    @NotNull(message = "El número de la pregunta es obligatorio.")
    private Integer number;

    @NotBlank(message = "La respuesta es obligatoria.")
    @Size(max = 255, message = "La respuesta no puede superar los 255 caracteres.")
    private String answer;

    public AnswerDto() {
    }

    public Integer getNumber() {
        return number;
    }

    public String getAnswer() {
        return answer;
    }

    public void setNumber(Integer number){
        this.number = number;
    }

    public void setAnswer(String answer){
        this.answer = answer;
    }
}