package com.example.services;

import com.example.dtos.group.GroupResponseDto;
import com.example.dtos.group.JoinGroupDto;
import com.example.exceptions.NotFoundException;
import com.example.exceptions.ValidationException;
import com.example.models.group.Group;
import com.example.models.user.Role;
import com.example.models.user.User;
import com.example.repository.group.GroupRepository;
import com.example.repository.student.StudentRepository;

import java.util.List;

public class StudentService {

    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;

    public StudentService( StudentRepository studentRepository, GroupRepository groupRepository) {

        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
    }

    public void joinGroup(String studentId, JoinGroupDto dto) {

        User student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new NotFoundException("Alumno no encontrado."));

        if (student.getRole() != Role.STUDENT) {
            throw new ValidationException(List.of("Solo los alumnos pueden unirse a un grupo."));
        }

        if (student.getGroupId() != null) {
            throw new ValidationException(List.of("El alumno ya pertenece a un grupo."));
        }

        Group group = groupRepository.findByAccessCode(dto.getAccessCode())
                .orElseThrow(() ->
                        new NotFoundException("Código de grupo inválido."));

        studentRepository.joinGroup(student.getId(), group.getId());
    }

    public void leaveGroup(String studentId) {

        User student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new NotFoundException("Alumno no encontrado."));

        if (student.getRole() != Role.STUDENT) {
            throw new ValidationException(List.of("Solo los alumnos pertenecen a un grupo."));
        }

        if (student.getGroupId() == null) {
            throw new ValidationException(List.of("El alumno no pertenece a ningún grupo."));
        }

        boolean removed = studentRepository.leaveGroup(studentId);

        if (!removed) {
            throw new NotFoundException("No se pudo retirar al alumno del grupo.");
        }
    }

    public GroupResponseDto findGroup(String studentId) {

        User student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new NotFoundException("Alumno no encontrado."));

        if (student.getGroupId() == null) {
            throw new NotFoundException("El alumno no pertenece a ningún grupo.");
        }

        Group group = groupRepository.findById(student.getGroupId())
                .orElseThrow(() ->
                        new NotFoundException("El grupo del alumno no existe."));

        return new GroupResponseDto(
                group.getId(),
                group.getName(),
                group.getSemester(),
                group.getGroup(),
                group.getAccessCode()
        );
    }
}