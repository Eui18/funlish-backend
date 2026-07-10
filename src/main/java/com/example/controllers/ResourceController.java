package com.example.controllers;

import com.example.dtos.resource.CreateResourceDto;
import com.example.dtos.resource.UpdateResourceDto;
import com.example.services.ResourceService;
import com.example.utils.DtoValidator;

import io.javalin.http.Context;

public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    public void create(Context context) {

        String topicId = context.pathParam("topicId");

        CreateResourceDto dto = context.bodyAsClass(CreateResourceDto.class);

        DtoValidator.validate(dto);

        context.status(201).json(
                resourceService.create(topicId, dto)
        );
    }

    public void findAll(Context context) {

        String topicId = context.pathParam("topicId");

        context.json(
                resourceService.findAll(topicId)
        );
    }

    public void findById(Context context) {

        String id = context.pathParam("id");

        context.json(
                resourceService.findById(id)
        );
    }

    public void update(Context context) {

        String id = context.pathParam("id");

        UpdateResourceDto dto = context.bodyAsClass(UpdateResourceDto.class);

        DtoValidator.validate(dto);

        context.json(
                resourceService.update(id, dto)
        );
    }

    public void delete(Context context) {

        String id = context.pathParam("id");

        resourceService.delete(id);

        context.status(204);
    }
}