package com.example.controllers;

import com.example.dtos.MessageResponseDto;
import com.example.dtos.forum.CreateCommentDto;
import com.example.dtos.forum.CreateForumDto;
import com.example.services.ForumService;
import com.example.utils.DtoValidator;

import io.javalin.http.Context;

public class ForumController {

    private final ForumService service;

    public ForumController(ForumService service) {
        this.service = service;
    }

    public void findAll(Context context) {

        String groupId = context.pathParam("groupId");
        var response = service.findAll(groupId);
        context.json(response);
    }

    public void findById(Context context) {

        String id = context.pathParam("id");
        var response = service.findById(id);
        context.json(response);
    }

    public void create(Context context) {

        String groupId = context.pathParam("groupId");
        CreateForumDto dto = context.bodyAsClass(CreateForumDto.class);
        DtoValidator.validate(dto);

        var response = service.create(groupId, dto);
        context.status(201).json(response);
    }

    public void delete(Context context) {

        String id = context.pathParam("id");
        String teacherId = context.pathParam("teacherId");

        service.delete(id, teacherId);

        context.status(200).json(new MessageResponseDto("Publicación eliminada correctamente"));
    }

    public void addComment(Context context) {

        String forumId = context.pathParam("forumId");
        CreateCommentDto dto = context.bodyAsClass(CreateCommentDto.class);
        DtoValidator.validate(dto);

        var response = service.addComment(forumId, dto);
        context.status(201).json(response);
    }

    public void deleteComment(Context context) {

        String commentId = context.pathParam("commentId");
        String requesterId = context.pathParam("requesterId");

        service.deleteComment(commentId, requesterId);

        context.status(200).json(new MessageResponseDto("Comentario eliminado correctamente"));
    }
}