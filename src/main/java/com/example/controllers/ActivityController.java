package com.example.controllers;

import com.example.dtos.activity.CreateActivityDto;
import com.example.dtos.activity.UpdateActivityDto;
import com.example.services.ActivityService;
import com.example.utils.DtoValidator;
import io.javalin.http.Context;

public class ActivityController {
    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    public void create(Context context) {
        String topicId = context.pathParam("topicId");
        String teacherId = context.pathParam("teacherId");
        CreateActivityDto dto = context.bodyAsClass(CreateActivityDto.class);
        DtoValidator.validate(dto);
        context.status(201).json(activityService.create(topicId, teacherId, dto));
    }

    public void findAll(Context context) {
        String topicId = context.pathParam("topicId");
        context.json(activityService.findAll(topicId));
    }

    public void findById(Context context) {
        String id = context.pathParam("id");
        context.json(activityService.findById(id));
    }

    public void update(Context context) {
        String id = context.pathParam("id");
        UpdateActivityDto dto = context.bodyAsClass(UpdateActivityDto.class);
        DtoValidator.validate(dto);
        context.json(activityService.update(id, dto));
    }

    public void delete(Context context) {
        String id = context.pathParam("id");
        activityService.delete(id);
        context.status(204);
    }
}