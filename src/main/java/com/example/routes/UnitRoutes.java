package com.example.routes;

import com.example.controllers.UnitController;
import io.javalin.Javalin;

public class UnitRoutes {

    private final UnitController unitController;

    public UnitRoutes(UnitController unitController) {
        this.unitController = unitController;
    }

    public void register(Javalin app) {

        // Teacher / Student - Get units of a group
        app.get("/groups/{groupId}/units", unitController::findAll);

        // Teacher - Create unit
        app.post("/groups/{groupId}/units", unitController::create);

        // Teacher / Student - Get unit by id
        app.get("/units/{id}", unitController::findById);

        // Teacher - Update unit
        app.patch("/units/{id}", unitController::update);

        // Teacher - Delete unit
        app.delete("/units/{id}", unitController::delete);
        }
}