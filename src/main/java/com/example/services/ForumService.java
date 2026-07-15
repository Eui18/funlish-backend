package com.example.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.dtos.forum.CommentResponseDto;
import com.example.dtos.forum.CreateCommentDto;
import com.example.dtos.forum.CreateForumDto;
import com.example.dtos.forum.ForumResponseDto;
import com.example.exceptions.NotFoundException;
import com.example.exceptions.ValidationException;
import com.example.models.forum.Comment;
import com.example.models.forum.Forum;
import com.example.models.user.Role;
import com.example.models.user.User;
import com.example.repository.auth.AuthRepository;
import com.example.repository.forum.CommentRepository;
import com.example.repository.forum.ForumRepository;
import com.example.repository.group.GroupRepository;

public class ForumService {

    private final ForumRepository repository;
    private final CommentRepository commentRepository;
    private final GroupRepository groupRepository;
    private final AuthRepository authRepository;

    public ForumService(
            ForumRepository repository,
            CommentRepository commentRepository,
            GroupRepository groupRepository,
            AuthRepository authRepository) {

        this.repository = repository;
        this.commentRepository = commentRepository;
        this.groupRepository = groupRepository;
        this.authRepository = authRepository;
    }


    public List<ForumResponseDto> findAll(String groupId) {

        groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Grupo no encontrado."));

        List<Forum> forums = repository.findAll(groupId);

        List<ForumResponseDto> response = new ArrayList<>();

        for (Forum forum : forums) {
            response.add(toResponseDto(forum));
        }

        return response;
    }


    public ForumResponseDto findById(String id) {

        Forum forum = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Publicación no encontrada."));

        return toResponseDto(forum);
    }


    public ForumResponseDto create(String groupId, CreateForumDto dto) {

        groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Grupo no encontrado."));

        User teacher = authRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new NotFoundException("El docente no existe."));

        if (teacher.getRole() != Role.TEACHER) {
            throw new ValidationException(List.of("Solo un docente puede crear publicaciones."));
        }

        Forum forum = new Forum(
                UUID.randomUUID().toString(),
                groupId,
                dto.getTeacherId(),
                dto.getTitle(),
                dto.getDescription(),
                dto.getUrl(),
                LocalDateTime.now()
        );

        repository.create(forum);

        return toResponseDto(forum);
    }


    public void delete(String id, String teacherId) {

        repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Publicación no encontrada."));

        User teacher = authRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException("El docente no existe."));

        if (teacher.getRole() != Role.TEACHER) {
            throw new ValidationException(List.of("Solo un docente puede eliminar publicaciones."));
        }

        boolean deleted = repository.delete(id);

        if (!deleted) {
            throw new NotFoundException("No se pudo eliminar la publicación.");
        }
    }


    public CommentResponseDto addComment(String forumId, CreateCommentDto dto) {

        repository.findById(forumId)
                .orElseThrow(() -> new NotFoundException("Publicación no encontrada."));

        User user = authRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("El usuario no existe."));

        Comment comment = new Comment(
                UUID.randomUUID().toString(),
                forumId,
                dto.getUserId(),
                dto.getComment(),
                LocalDateTime.now()
        );

        commentRepository.create(comment);

        return new CommentResponseDto(
                comment.getId(),
                user.getId(),
                user.getName(),
                comment.getComment(),
                comment.getCreatedAt().toString()
        );
    }


    public void deleteComment(String commentId, String requesterId) {

        commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comentario no encontrado."));

        User requester = authRepository.findById(requesterId)
                .orElseThrow(() -> new NotFoundException("El usuario no existe."));

        if (requester.getRole() != Role.TEACHER) {
            throw new ValidationException(List.of("Solo un docente puede eliminar comentarios."));
        }

        boolean deleted = commentRepository.delete(commentId);

        if (!deleted) {
            throw new NotFoundException("No se pudo eliminar el comentario.");
        }
    }


    private ForumResponseDto toResponseDto(Forum forum) {

        List<Comment> comments = commentRepository.findAllByForum(forum.getId());

        List<CommentResponseDto> commentDtos = new ArrayList<>();

        for (Comment comment : comments) {

            User user = authRepository.findById(comment.getUserId()).orElse(null);
            String userName = user != null ? user.getName() : "Usuario eliminado";

            commentDtos.add(new CommentResponseDto(
                    comment.getId(),
                    comment.getUserId(),
                    userName,
                    comment.getComment(),
                    comment.getCreatedAt().toString()
            ));
        }

        return new ForumResponseDto(
                forum.getId(),
                forum.getGroupId(),
                forum.getTeacherId(),
                forum.getTitle(),
                forum.getDescription(),
                forum.getUrl(),
                forum.getCreatedAt().toString(),
                commentDtos
        );
    }
}