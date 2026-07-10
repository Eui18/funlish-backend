package com.example.dtos.resource;

public class ResourceResponseDto {

    private String id;
    private String name;
    private String type;
    private String description;
    private String url;

    public ResourceResponseDto(
            String id,
            String name,
            String type,
            String description,
            String url
    ) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }
}