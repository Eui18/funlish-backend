package com.example.models.unit;

public class Unit {

    private String id;
    private String groupId;
    private String name;
    private Integer number;

    public Unit() {
    }

    public Unit(String id, String groupId, String name, Integer number) {
        this.id = id;
        this.groupId = groupId;
        this.name = name;
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}