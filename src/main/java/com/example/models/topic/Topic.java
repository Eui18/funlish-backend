package com.example.models.topic;

public class Topic {

    private String id;
    private String unitId;
    private Integer number;
    private String title;
    private String description;

    public Topic() {
    }

    public Topic(
            String id,
            String unitId,
            Integer number,
            String title,
            String description
    ) {
        this.id = id;
        this.unitId = unitId;
        this.number = number;
        this.title = title;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}