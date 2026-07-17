package com.example.repository.forum;

import java.util.List;
import java.util.Optional;

import com.example.models.forum.Forum;

public interface ForumRepository {

    List<Forum> findAll(String groupId);

    Optional<Forum> findById(String id);

    Forum create(Forum forum);

    boolean update(Forum forum);

    boolean delete(String id);
}