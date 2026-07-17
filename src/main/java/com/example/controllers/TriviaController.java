package com.example.controllers;

import java.util.Map;

import com.example.dtos.trivia.CreateTriviaDto;
import com.example.dtos.trivia.UpdateTriviaDto;
import com.example.services.TriviaService;

import io.javalin.http.Context;

public class TriviaController {

    private final TriviaService triviaService;

    public TriviaController(TriviaService triviaService) {
        this.triviaService = triviaService;
    }

    public void create(Context context) {

        CreateTriviaDto dto = context.bodyAsClass(CreateTriviaDto.class);
        String teacherId = context.attribute("userId");

        triviaService.create(context.pathParam("activityId"), dto, teacherId);

        context.status(201).json(Map.of("message", "Pregunta creada correctamente."));
    }

    public void findAll(Context context) {

        context.json(triviaService.findAll(context.pathParam("activityId")));
    }

    public void update(Context context){

        UpdateTriviaDto dto = context.bodyAsClass(UpdateTriviaDto.class);

        triviaService.update(context.pathParam("id"), dto);

        context.json(Map.of(
                    "message",
                    "Pregunta actualizada correctamente."
                )
        );
    }

    public void delete(Context context) {

        triviaService.delete(context.pathParam("id"));

        context.json(Map.of("message", "Pregunta eliminada correctamente."));
    }
}