package com.example.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import com.example.dtos.group.CreateGroupDto;
import com.example.dtos.group.GroupResponseDto;
import com.example.dtos.group.UpdateGroupDto;
import com.example.dtos.users.UserResponseDto;
import com.example.exceptions.NotFoundException;
import com.example.exceptions.ResourceAlreadyExistsException;
import com.example.exceptions.ValidationException;
import com.example.models.group.Group;
import com.example.models.user.Role;
import com.example.models.user.User;
import com.example.repository.auth.AuthRepository;
import com.example.repository.group.GroupRepository;
import com.example.repository.profile.ProfileRepository;
import com.example.repository.student.StudentRepository;
import com.example.utils.OwnershipValidator;

public class GroupService {

    private final GroupRepository repository;
    private final AuthRepository authRepository;
    private final StudentRepository studentRepository;
    private final ProfileRepository profileRepository;
    private final OwnershipValidator ownershipValidator;

    public GroupService(
            GroupRepository repository,
            AuthRepository authRepository,
            StudentRepository studentRepository,
            ProfileRepository profileRepository,
            OwnershipValidator ownershipValidator) {

        this.repository = repository;
        this.authRepository = authRepository;
        this.studentRepository = studentRepository;
        this.profileRepository = profileRepository;
        this.ownershipValidator = ownershipValidator;
    }

   public List<GroupResponseDto> findAll(String teacherId) {

        Optional<User> user = authRepository.findById(teacherId);

        if (user.isEmpty()) {
            throw new NotFoundException("El docente no existe");
        }

        if (user.get().getRole() != Role.TEACHER) {
            throw new ValidationException(
                    List.of("El usuario no es docente")
            );
        }

        List<Group> groups = repository.findAll(teacherId);

        List<GroupResponseDto> response = new ArrayList<>();

        for (Group group : groups) {
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


    public GroupResponseDto create(CreateGroupDto dto, String teacherId) {

        Optional<User> user = authRepository.findById(teacherId);

        if (user.isEmpty()) {
            throw new NotFoundException("El docente no existe");
        }

        if (user.get().getRole() != Role.TEACHER) {
            throw new ValidationException(List.of("El usuario no es docente")
            );
        }

        Optional<Group> optional = repository.findExist(
                dto.getSemester(),
                dto.getGroup(),
                teacherId
        );

        if (optional.isPresent()) {
            throw new ResourceAlreadyExistsException("Ya existe un grupo con ese nombre.");
        }

        String code = generateCode();

        Group group = new Group(
                UUID.randomUUID().toString(),
                dto.getName(),
                dto.getSemester(),
                dto.getGroup(),
                code,
                teacherId
        );

        repository.create(group);
        return toResponseDto(group);
    }


    public GroupResponseDto update(String id, UpdateGroupDto dto, String teacherId) {

        ownershipValidator.assertTeacherOwnsGroup(id, teacherId);

        Group group = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Grupo no encontrado.")
                );
        if (dto.getName() != null) {

            if (dto.getName().isBlank()) {
                throw new ValidationException(
                        List.of("El nombre del grupo no puede estar vacío.")
                );
            }
            group.setName(dto.getName());
        }
        boolean updated = repository.update(group);

        if (!updated) {
            throw new NotFoundException("No se pudo actualizar el grupo.");
        }
        return toResponseDto(group);
    }


    public void delete(String id, String teacherId) {

        ownershipValidator.assertTeacherOwnsGroup(id, teacherId);

        boolean deleted = repository.delete(id);

        if (!deleted) {
            throw new NotFoundException("Grupo no encontrado.");
        }
    }


   public List<UserResponseDto> findStudents(String groupId) {
        repository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Grupo no encontrado."));
        return repository.findStudents(groupId);
    }

    public int countStudents(String groupId) {
        repository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Grupo no encontrado."));
        return repository.countStudents(groupId);
    }


    // Perfil completo de un alumno del grupo, consultado por el docente dueño del grupo.
    public Map<String, Object> getStudentProfileForTeacher(String groupId, String studentId, String teacherId) {

        ownershipValidator.assertTeacherOwnsGroup(groupId, teacherId);

        User student = authRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Alumno no encontrado."));

        if (student.getRole() != Role.STUDENT || !groupId.equals(student.getGroupId())) {
            throw new ValidationException(
                    List.of("El alumno no pertenece a este grupo.")
            );
        }

        var profile = profileRepository.findStudentProfile(studentId)
                .orElseThrow(() -> new NotFoundException("Alumno no encontrado."));

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("profile", profile);
        response.put("statistics", profileRepository.findStudentStatistics(studentId));
        response.put("ranking", profileRepository.findStudentRanking(studentId));
        response.put("themes", profileRepository.findThemePerformance(studentId));
        response.put("activities", profileRepository.findActivityPerformance(studentId));

        return response;
    }


    // Desvincula a un alumno del grupo; solo el docente dueño del grupo puede hacerlo.
    public void removeStudent(String groupId, String studentId, String teacherId) {

        ownershipValidator.assertTeacherOwnsGroup(groupId, teacherId);

        User student = authRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Alumno no encontrado."));

        if (student.getRole() != Role.STUDENT || !groupId.equals(student.getGroupId())) {
            throw new ValidationException(
                    List.of("El alumno no pertenece a este grupo.")
            );
        }

        boolean removed = studentRepository.leaveGroup(studentId);

        if (!removed) {
            throw new NotFoundException("No se pudo desvincular al alumno del grupo.");
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
                group.getGroup(),
                group.getAccessCode()
        );
    }
}
