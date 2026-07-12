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

   public void create(Context context) {

        CreateActivityDto dto = context.bodyAsClass(CreateActivityDto.class);

        String teacherId = context.pathParam("teacherId");
        String topicId = context.pathParam("topicId");

        activityService.create(dto, teacherId, topicId);

        context.status(201).json(Map.of(
                "message", "Actividad creada correctamente."));
    }


    public void update(Context context) {

        UpdateActivityDto dto = context.bodyAsClass(UpdateActivityDto.class);

        activityService.update(
                context.pathParam("id"),
                dto);

        context.json(Map.of(
                "message", "Actividad actualizada correctamente."));
    }

    public void publish(Context context) {

        activityService.publish(context.pathParam("id"));

        context.json(Map.of(
                "message", "Actividad publicada."));
    }

    public void delete(Context context) {

        activityService.delete(context.pathParam("id"));

        context.json(Map.of(
                "message", "Actividad eliminada."));
    }

    public void findById(Context context) {

        context.json(activityService.findById(context.pathParam("id")));
    }

    public void findAll(Context context) {

        context.json(activityService.findAll(context.pathParam("topicId")));
    }

}