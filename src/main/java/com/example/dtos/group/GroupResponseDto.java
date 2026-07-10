package com.example.dtos.group;

public class GroupResponseDto {
    private String id;
    private String name;
    private Integer semester;
    private String group;
    private String accessCode;

    public GroupResponseDto (String id, String name, Integer semester, String group, String accessCode) {
        this.id = id;
        this.name = name;
        this.semester = semester;
        this.group = group;
        this.accessCode = accessCode;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getSemester() {
        return semester;
    }

    public String getGroup() {
        return group;
    }

    public String getAccessCode() {
        return accessCode;
    }
}
