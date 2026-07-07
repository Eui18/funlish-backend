package com.example.models.group;

public class Group {

    private String id;
    private String teacherId;
    private String name;
    private int semester;
    private String accessCode;


    public Group() {
    }

    public Group(
            String id,
            String name,
            Integer semester,
            String accessCode,
            String teacherId) {
        this.id = id;
        this.name = name;
        this.semester = semester;
        this.accessCode = accessCode;
        this.teacherId = teacherId;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSemester() {
        return this.semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public String getAccessCode() {
        return this.accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getTeacherId() {
        return this.teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

}