package com.example.dtos.group;

public class GroupResponseDto {
    private String id;
    private String name;
    private Integer semester;
    private String accessCode;
    private String teacherId;

    public GroupResponseDto (String id, String name, Integer semester, String accessCode, String teacherId) {
        this.id = id;
        this.name = name;
        this.semester = semester;
        this.accessCode = accessCode;
        this.teacherId = teacherId; 
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

    public String getAccessCode() {
        return accessCode;
    }

    public String getTeacherId() {
        return teacherId;
    }

}
