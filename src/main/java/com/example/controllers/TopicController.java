package com.example.controllers;

import com.example.dtos.topic.CreateTopicDto;
import com.example.dtos.topic.UpdateTopicDto;
import com.example.services.TopicService;
import com.example.utils.DtoValidator;

import io.javalin.http.Context;

public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    // Crear tema dentro de una unidad
    public void create(Context context) {

        String unitId = context.pathParam("unitId");

        CreateTopicDto dto = context.bodyAsClass(CreateTopicDto.class);
        DtoValidator.validate(dto); // CORRECCIÓN: Agregado
        String teacherId = context.attribute("userId");

        context.status(201)
                .json(topicService.create(unitId, dto, teacherId));
    }

    // Obtener temas de una unidad
    public void findAll(Context context) {

        String unitId = context.pathParam("unitId");

        context.json(
                topicService.findAll(unitId)
        );
    }

    // Obtener tema por id
    public void findById(Context context) {

        String id = context.pathParam("id");

        context.json(
                topicService.findById(id)
        );
    }

    // Actualizar tema
    public void update(Context context) {

        String id = context.pathParam("id");

        UpdateTopicDto dto = context.bodyAsClass(UpdateTopicDto.class);
        DtoValidator.validate(dto); // CORRECCIÓN: Agregado

        context.json(
                topicService.update(id, dto)
        );
    }

    // Eliminar tema
    public void delete(Context context) {

        String id = context.pathParam("id");

        topicService.delete(id);

        context.status(204); // No Content
    }
}