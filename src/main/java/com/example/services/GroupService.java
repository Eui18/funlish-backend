package com.example.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import com.example.dtos.group.CreateGroupDto;
import com.example.dtos.group.GroupResponseDto;
import com.example.dtos.group.UpdateGroupDto;
import com.example.exceptions.NotFoundException;
import com.example.exceptions.ResourceAlreadyExistsException;
import com.example.exceptions.ValidationException;
import com.example.models.group.Group;
import com.example.models.user.Role;
import com.example.models.user.User;
import com.example.repository.auth.AuthRepository;
import com.example.repository.group.GroupRepository;

public class GroupService {

    private final GroupRepository repository;
    private final AuthRepository authRepository;

    public GroupService(GroupRepository repository, AuthRepository authRepository) {
        this.repository = repository;
        this.authRepository = authRepository;
    }

    public List<GroupResponseDto> findAll(String teacherId) {

        List<Group> groups = repository.findAll(teacherId);
        List<GroupResponseDto> response = new ArrayList<>();

        for(Group group : groups){
            response.add(toResponseDto(group));
        }
        return response;
    }


    public GroupResponseDto findById(String id) {

        Optional<Group> optional = repository.findById(id);

        if (optional.isEmpty()) {
            throw new NotFoundException("Grupo no encontrado.");
        }

        Group group = optional.get();
        return toResponseDto(group);
    }


    public GroupResponseDto create(CreateGroupDto dto) {

        Optional<User> user = authRepository.findById(dto.getTeacherId());

        if (user.isEmpty()) {
            throw new NotFoundException("El docente no existe");
        }

        if (user.get().getRole() != Role.TEACHER) {
            throw new ValidationException(List.of("El usuario no es docente")
            );
        }

        Optional<Group> optional = repository.findExist(
                dto.getName(),
                dto.getTeacherId()
        );

        if (optional.isPresent()) {
            throw new ResourceAlreadyExistsException("Ya existe un grupo con ese nombre.");
        }

        String code = generateCode();

        Group group = new Group(
                UUID.randomUUID().toString(),
                dto.getName(),
                dto.getSemester(),
                code,
                dto.getTeacherId()
        );

        repository.create(group);
        return toResponseDto(group);
    }


    public GroupResponseDto update(String id, UpdateGroupDto dto) {

        Optional<Group> optional = repository.findById(id);

        if (optional.isEmpty()) {
            throw new NotFoundException("Grupo no encontrado.");
        }

        Group group = optional.get();

        if (dto.getName() != null) {
            group.setName(dto.getName());
        }

        if (dto.getSemester() != null) {
            group.setSemester(dto.getSemester());
        }

        boolean updated = repository.update(group);

        if (!updated) {
            throw new NotFoundException("No se pudo actualizar el grupo.");
        }
        return toResponseDto(group);
    }


    public void delete(String id) {

        boolean deleted = repository.delete(id);

        if (!deleted) {
            throw new NotFoundException("Grupo no encontrado.");
        }
    }


    private String generateCode() {

        String code;

        do {
            code = randomCode();
        } while (repository.existsByAccessCode(code));

        return code;
    }


    private String randomCode() {

        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }

        return code.toString();
    }


    private GroupResponseDto toResponseDto(Group group) {

        return new GroupResponseDto(
                group.getId(),
                group.getName(),
                group.getSemester(),
                group.getAccessCode(),
                group.getTeacherId()
        );
    }
}