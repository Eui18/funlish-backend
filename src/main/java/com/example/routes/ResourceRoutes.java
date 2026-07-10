package com.example.routes;

import com.example.controllers.ResourceController;

import io.javalin.Javalin;

public class ResourceRoutes {

    private final ResourceController resourceController;

    public ResourceRoutes(ResourceController resourceController) {
        this.resourceController = resourceController;
    }

    public void register(Javalin app) {

        // Recursos de un tema
        app.get(
                "/topics/{topicId}/resources",
                resourceController::findAll
        );

        // Crear recurso
        app.post(
                "/topics/{topicId}/resources",
                resourceController::create
        );

        // Obtener recurso por id
        app.get(
                "/resources/{id}",
                resourceController::findById
        );

        // Actualizar recurso
        app.patch(
                "/resources/{id}",
                resourceController::update
        );

        // Eliminar recurso
        app.delete(
                "/resources/{id}",
                resourceController::delete
        );
    }
}