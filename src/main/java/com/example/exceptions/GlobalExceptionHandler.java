package com.example.exceptions;

import io.javalin.Javalin;

public class GlobalExceptionHandler {

    public void register(Javalin app) {

        app.exception(NotFoundException.class, (e, ctx) -> {
            ctx.status(404).json(
                new ErrorResponse(e.getMessage())
            );
        });


        app.exception(ResourceAlreadyExistsException.class, (e, ctx) -> {
            ctx.status(409).json(
                new ErrorResponse(e.getMessage())
            );
        });


        app.exception(UnauthorizedException.class, (e, ctx) -> {
            ctx.status(401).json(
                new ErrorResponse(e.getMessage())
            );
        });


        app.exception(ValidationException.class, (e, ctx) -> {
            ctx.status(400).json(
                new ErrorResponse(e.getMessage())
            );
        });

        app.exception(Exception.class, (e, ctx) -> {

            e.printStackTrace();

            ctx.status(500).json(
                new ErrorResponse(
                    "Error interno: " + e.getMessage()
                )
            );
        });
    }
}