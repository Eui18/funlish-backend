package com.example.dtos.group;

public class UpdateGroupDto {

    private String name;

    public UpdateGroupDto() {
    }

    public UpdateGroupDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}