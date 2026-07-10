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

    @NotBlank(message = "El docente es obligatorio")
    private String teacherId;
    
    public CreateGroupDto(){
    }

    public CreateGroupDto(
            String name,
            Integer semester,
            String group,
            String teacherId) {

        this.name = name;
        this.semester = semester;
        this.group = group;
        this.teacherId = teacherId;
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

    public String getTeacherId (){
        return this.teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }
}