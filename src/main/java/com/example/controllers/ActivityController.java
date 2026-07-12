package com.example.controllers;

import io.javalin.http.Context;

import java.util.Map;

import com.example.dtos.activity.CreateActivityDto;
import com.example.dtos.activity.UpdateActivityDto;
import com.example.services.ActivityService;

public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    public void create(Context ctx) {

        CreateActivityDto dto = ctx.bodyAsClass(CreateActivityDto.class);

        String teacherId = ctx.pathParam("teacherId");

        activityService.create(dto, teacherId);

        ctx.status(201).json(Map.of(
                "message", "Actividad creada correctamente."));
    }

    public void update(Context ctx) {

        UpdateActivityDto dto = ctx.bodyAsClass(UpdateActivityDto.class);

        activityService.update(
                ctx.pathParam("id"),
                dto);

        ctx.json(Map.of(
                "message", "Actividad actualizada correctamente."));
    }

    public void publish(Context ctx) {

        activityService.publish(ctx.pathParam("id"));

        ctx.json(Map.of(
                "message", "Actividad publicada."));
    }

    public void delete(Context ctx) {

        activityService.delete(ctx.pathParam("id"));

        ctx.json(Map.of(
                "message", "Actividad eliminada."));
    }

    public void findById(Context ctx) {

        ctx.json(activityService.findById(ctx.pathParam("id")));
    }

    public void findAll(Context ctx) {

        ctx.json(activityService.findAll(ctx.pathParam("topicId")));
    }

}