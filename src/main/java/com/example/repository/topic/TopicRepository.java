package com.example.repository.topic;

import java.util.List;
import java.util.Optional;

import com.example.models.topic.Topic;

public interface TopicRepository {

    Topic create(Topic topic);

    List<Topic> findAll(String unitId);

    Optional<Topic> findById(String id);

    boolean update(Topic topic);

    boolean delete(String id);

    int nextNumber(String unitId);

    boolean existsTitle(String unitId, String title);

}