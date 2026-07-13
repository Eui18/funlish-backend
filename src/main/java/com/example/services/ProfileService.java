package com.example.services;

import java.util.LinkedHashMap;
import java.util.Map;

import com.example.dtos.profile.StudentProfileDto;
import com.example.dtos.profile.TeacherDashboardDto;
import com.example.exceptions.NotFoundException;
import com.example.repository.profile.ProfileRepository;

public class ProfileService {

    private final ProfileRepository repository;

    public ProfileService(ProfileRepository repository) {
        this.repository = repository;
    }


    // PERFIL DEL ALUMNO

    public Map<String, Object> getStudentProfile(String studentId) {

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