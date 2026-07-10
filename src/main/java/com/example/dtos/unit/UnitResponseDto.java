package com.example.dtos.unit;

public class UnitResponseDto {

    private String id;
    private String name;
    private Integer number;

    public UnitResponseDto(String id, String name, Integer number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getNumber() {
        return number;
    }
}