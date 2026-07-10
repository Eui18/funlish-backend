package com.example.routes;

import com.example.controllers.TopicController;
import io.javalin.Javalin;

public class TopicRoutes {

    private final TopicController topicController;

    public TopicRoutes(TopicController topicController) {
        this.topicController = topicController;
    }

    public void register(Javalin app) {

        // Crear tema dentro de una unidad
        app.post("/units/{unitId}/topics", topicController::create);

        // Ver temas de una unidad
        app.get("/units/{unitId}/topics", topicController::findAll);

        // Ver tema por id
        app.get("/topics/{id}", topicController::findById);

        // Editar tema
        app.patch("/topics/{id}", topicController::update);

        // Eliminar tema
        app.delete("/topics/{id}", topicController::delete);
    }
}