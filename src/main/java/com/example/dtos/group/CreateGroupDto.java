package com.example.dtos.group;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateGroupDto {
    
    @NotBlank(message = "El nombre es obligatorio")
    private String name; 

    @NotNull(message = "El cuatrimestre es obligatorio")
    @Min(value = 1, message = "El cuatrimestre debe de ser mayor o igual a 1" )
    @Max(value = 11, message = "El cuatrimestre no es válido")
    private Integer semester;

    @NotBlank
    @Pattern(
        regexp = "^[A-Z]$",
        message = "El grupo debe ser una sola letra mayúscula."
    )
    private String group;

    public CreateGroupDto(){
    }

    public CreateGroupDto(
            String name,
            Integer semester,
            String group) {

        this.name = name;
        this.semester = semester;
        this.group = group;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSemester(){
        return this.semester;
    }

    public void setSemester(Integer semester){
        this.semester = semester;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}