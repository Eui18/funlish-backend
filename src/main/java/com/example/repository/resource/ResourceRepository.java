package com.example.repository.resource;

import java.util.List;
import java.util.Optional;

import com.example.models.resource.Resource;

public interface ResourceRepository {

    Resource create(Resource resource);

    List<Resource> findAll(String topicId);

    Optional<Resource> findById(String id);

    boolean update(Resource resource);

    boolean delete(String id);

    boolean existsByName(String topicId, String name);
}