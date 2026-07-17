package com.example.controllers;

import java.util.Map;

import com.example.dtos.MessageResponseDto;
import com.example.dtos.group.CreateGroupDto;
import com.example.dtos.group.UpdateGroupDto;
import com.example.services.GroupService;
import com.example.utils.DtoValidator;

import io.javalin.http.Context;


public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    public void create(Context context) {

        CreateGroupDto dto = context.bodyAsClass(CreateGroupDto.class);
        DtoValidator.validate(dto);
        String teacherId = context.attribute("userId");
        var response = groupService.create(dto, teacherId);
        context.status(201).json(response);
    }

    public void findAll(Context context) {

        String teacherId = context.attribute("userId");
        var response = groupService.findAll(teacherId);
        context.json(response);
    }

    public void findById(Context context) {

        String id = context.pathParam("id");
        var response = groupService.findById(id);
        context.json(response);
    }

    public void update(Context context) {

        String id = context.pathParam("id");
        UpdateGroupDto dto = context.bodyAsClass(UpdateGroupDto.class);
        DtoValidator.validate(dto);
        var response = groupService.update(id, dto);
        context.json(response);
    }

    public void delete(Context context) {

        String id = context.pathParam("id");
        groupService.delete(id);

        context.status(200).json(new MessageResponseDto("Grupo eliminado correctamente"));
    }

    public void findStudents(Context context) {

        String groupId = context.pathParam("id");
        var response = groupService.findStudents(groupId);
        context.json(response);
    }

    public void countStudents(Context context) {

        String groupId = context.pathParam("id");
        int total = groupService.countStudents(groupId);
        context.json(Map.of("students", total));
    }

}