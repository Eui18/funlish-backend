package com.example.repository.group;

import java.util.List;
import java.util.Optional;

import com.example.models.group.Group;

public interface GroupRepository {

    //docente
    List<Group> findAll(String teacherId);
    Optional<Group> findById(String id);
    Group create(Group group);
    boolean update(Group group);
    boolean delete(String id);
    Optional<Group> findExist(String name, String teacherId);
    boolean existsByAccessCode(String code);

    //general
    Optional<Group> findByAccessCode(String accessCode);

}
