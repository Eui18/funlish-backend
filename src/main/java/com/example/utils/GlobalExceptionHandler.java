package com.example.utils;

import java.util.Map;
import com.example.exceptions.NotFoundException;
import com.example.exceptions.ResourceAlreadyExistsException;
import com.example.exceptions.UnauthorizedException;
import com.example.exceptions.ValidationException;
import io.javalin.Javalin;

public class GlobalExceptionHandler {
    public void register(Javalin app) {

        // 400 - VALIDATION
        app.exception(ValidationException.class, (e, ctx) -> {
            ctx.status(400).json(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        });

        // 401 - UNAUTHORIZED
        app.exception(UnauthorizedException.class, (e, ctx) -> {
            ctx.status(401).json(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        });

        // 404 - NOT FOUND
        app.exception(NotFoundException.class, (e, ctx) -> {
            ctx.status(404).json(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        });

        // 409 - CONFLICT
        app.exception(ResourceAlreadyExistsException.class, (e, ctx) -> {
            ctx.status(409).json(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        });

        // 400 - ILLEGAL ARGUMENT (validaciones de negocio que no usan ValidationException)
        app.exception(IllegalArgumentException.class, (e, ctx) -> {
            ctx.status(400).json(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        });

        // 500 - GENERAL
        app.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();

            ctx.status(500).json(Map.of(
                    "success", false,
                    "message", "Error interno del servidor"));
        });
    }
}