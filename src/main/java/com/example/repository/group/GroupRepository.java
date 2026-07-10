package com.example.repository.group;

import java.util.List;
import java.util.Optional;

import com.example.dtos.users.UserResponseDto;
import com.example.models.group.Group;

public interface GroupRepository {

    //docente
    List<Group> findAll(String teacherId);
    Optional<Group> findById(String id);
    Group create(Group group);
    boolean update(Group group);
    boolean delete(String id);
    Optional<Group> findExist(Integer semester, String group, String teacherId);
    boolean existsByAccessCode(String code);

    //general
    Optional<Group> findByAccessCode(String accessCode);
    List<UserResponseDto> findStudents(String groupId);
    int countStudents(String groupId);
}
