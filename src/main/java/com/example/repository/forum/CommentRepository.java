package com.example.repository.forum;

import java.util.List;
import java.util.Optional;

import com.example.models.forum.Comment;

public interface CommentRepository {

    List<Comment> findAllByForum(String forumId);

    Optional<Comment> findById(String id);

    Comment create(Comment comment);

    boolean delete(String id);
}