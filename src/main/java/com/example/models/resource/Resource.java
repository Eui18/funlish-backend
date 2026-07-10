package com.example.models.resource;

public class Resource {

    private String id;
    private String topicId;
    private String name;
    private ResourceType type;
    private String description;
    private String url;

    public Resource() {
    }

    public Resource(
            String id,
            String topicId,
            String name,
            ResourceType type,
            String description,
            String url
    ) {
        this.id = id;
        this.topicId = topicId;
        this.name = name;
         this.type = type;
        this.description = description;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}