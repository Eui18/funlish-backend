package com.example.services;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.example.dtos.profile.StudentProfileDto;
import com.example.dtos.profile.TeacherDashboardDto;
import com.example.exceptions.NotFoundException;
import com.example.exceptions.ValidationException;
import com.example.models.user.Role;
import com.example.models.user.User;
import com.example.repository.auth.AuthRepository;
import com.example.repository.profile.ProfileRepository;

public class ProfileService {

    private final ProfileRepository repository;
    private final AuthRepository authRepository;

    public ProfileService(ProfileRepository repository, AuthRepository authRepository) {
        this.repository = repository;
        this.authRepository = authRepository;
    }


    // PERFIL DEL ALUMNO

    public Map<String, Object> getStudentProfile(String studentId) {

        User user = authRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Alumno no encontrado."));

        if (user.getRole() != Role.STUDENT) {
            throw new ValidationException(List.of("Solo un alumno puede consultar este perfil."));
        }

        StudentProfileDto profile = repository.findStudentProfile(studentId)
            .orElseThrow(() -> 
                new NotFoundException("Alumno no encontrado.")
            );

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("profile", profile);
        response.put("statistics", repository.findStudentStatistics(studentId));
        response.put("ranking", repository.findStudentRanking(studentId));
        response.put("themes", repository.findThemePerformance(studentId));
        response.put("activities", repository.findActivityPerformance(studentId));

        return response;
    }


    // DASHBOARD DOCENTE

    public Map<String, Object> getTeacherDashboard(String teacherId) {

        User user = authRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException("El docente no existe."));

        if (user.getRole() != Role.TEACHER) {
            throw new ValidationException(List.of("Solo un docente puede consultar este dashboard."));
        }

        TeacherDashboardDto dashboard =
            repository.findTeacherDashboard(teacherId);

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("dashboard", dashboard);
        response.put("worstThemes", repository.findWorstThemes(teacherId));
        response.put("activityPerformance", repository.findActivityTypePerformance(teacherId));
        response.put("students", repository.findStudents(teacherId));

        return response;
    }
}