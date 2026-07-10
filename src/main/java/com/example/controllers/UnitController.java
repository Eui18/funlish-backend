package com.example.controllers;

import com.example.dtos.MessageResponseDto;
import com.example.dtos.unit.CreateUnitDto;
import com.example.dtos.unit.UpdateUnitDto;
import com.example.services.UnitService;
import com.example.utils.DtoValidator;

import io.javalin.http.Context;

public class UnitController {

    private final UnitService unitService;

    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    public void findAll(Context context) {

        String groupId = context.pathParam("groupId");
        var response = unitService.findAll(groupId);
        context.json(response);
    }

    public void findById(Context context) {

        String id = context.pathParam("id");
        var response = unitService.findById(id);
        context.json(response);
    }

    public void create(Context context) {

        String groupId = context.pathParam("groupId");
        CreateUnitDto dto = context.bodyAsClass(CreateUnitDto.class);
        DtoValidator.validate(dto);

        var response = unitService.create(groupId, dto);
        context.status(201).json(response);
    }

    public void update(Context context) {

        String id = context.pathParam("id");

        UpdateUnitDto dto = context.bodyAsClass(UpdateUnitDto.class);
        DtoValidator.validate(dto);

        var response = unitService.update(id, dto);
        context.json(response);
    }

    public void delete(Context context) {

        String id = context.pathParam("id");
        unitService.delete(id);

        context.status(204)
                .json(new MessageResponseDto("Unidad eliminada correctamente"));
    }
}