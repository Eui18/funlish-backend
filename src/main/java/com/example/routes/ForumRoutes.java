package com.example.routes;

import com.example.controllers.ForumController;

import io.javalin.Javalin;

public class ForumRoutes {

    private final ForumController controller;

    public ForumRoutes(ForumController controller) {
        this.controller = controller;
    }

    public void register(Javalin app) {

        // Docente / Alumno - listar publicaciones de un grupo
        app.get("/groups/{groupId}/forums", controller::findAll);

        // Docente - crear publicación
        app.post("/groups/{groupId}/forums", controller::create);

        // Docente / Alumno - ver una publicación con sus comentarios
        app.get("/forums/{id}", controller::findById);

        // Docente - eliminar publicación
        app.delete("/forums/{id}/teachers/{teacherId}", controller::delete);

        // Docente / Alumno - comentar
        app.post("/forums/{forumId}/comments", controller::addComment);

        // Docente - eliminar comentario
        app.delete("/comments/{commentId}/users/{requesterId}", controller::deleteComment);
    }
}