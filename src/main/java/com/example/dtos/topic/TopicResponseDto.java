package com.example.dtos.topic;

public class TopicResponseDto {

    private String id;
    private Integer number;
    private String title;
    private String description;

    public TopicResponseDto(
            String id,
            Integer number,
            String title,
            String description
    ) {
        this.id = id;
        this.number = number;
        this.title = title;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public Integer getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}