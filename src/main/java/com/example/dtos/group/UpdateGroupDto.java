package com.example.dtos.group;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateGroupDto {

    private String name;

    @Min(value = 1, message = "El cuatrimestre debe ser mayor o igual a 1")
    @Max(value = 11, message = "El cuatrimestre no es válido")
    private Integer semester;


    public UpdateGroupDto() {
    }

    public UpdateGroupDto(String name, Integer semester) {
        this.name = name;
        this.semester = semester;
    }

    public String getName() {
        return name;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }
}