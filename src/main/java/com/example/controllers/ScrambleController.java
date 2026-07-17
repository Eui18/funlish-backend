package com.example.controllers;

import com.example.dtos.scramble.CreateScrambleDto;
import com.example.services.ScrambleService;

import io.javalin.http.Context;

public class ScrambleController {

    private final ScrambleService service;

    public ScrambleController(ScrambleService service) {
        this.service = service;
    }

    public void create(Context context) {

        String activityId = context.pathParam("activityId");

        CreateScrambleDto dto = context.bodyAsClass(CreateScrambleDto.class);
        String teacherId = context.attribute("userId");

        context.status(201).json(service.create(activityId, dto, teacherId));
    }


    public void findAll(Context context) {

        String activityId = context.pathParam("activityId");

        context.json( service.findAll(activityId));
    }


    public void delete(Context context) {

        String id = context.pathParam("id");

        service.delete(id);

        context.json(java.util.Map.of( "message", "Scramble eliminado correctamente."
                )
        );
    }
}