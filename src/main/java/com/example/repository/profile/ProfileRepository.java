package com.example.repository.profile;

import java.util.List;
import java.util.Optional;

import com.example.dtos.profile.ActivityPerformanceDto;
import com.example.dtos.profile.StudentRankingDto;
import com.example.dtos.profile.StudentSummaryDto;
import com.example.dtos.profile.StudentStatisticsDto;
import com.example.dtos.profile.TeacherDashboardDto;
import com.example.dtos.profile.ThemePerformanceDto;
import com.example.dtos.profile.StudentProfileDto;

public interface ProfileRepository {

    // PERFIL DEL ALUMNO

    Optional<StudentProfileDto> findStudentProfile(String studentId);
    StudentStatisticsDto findStudentStatistics(String studentId);
    StudentRankingDto findStudentRanking(String studentId);
    List<ThemePerformanceDto> findThemePerformance(String studentId);
    List<ActivityPerformanceDto> findActivityPerformance(String studentId);

    // DASHBOARD DOCENTE

    TeacherDashboardDto findTeacherDashboard(String teacherId);
    List<ThemePerformanceDto> findWorstThemes(String teacherId);
    List<ActivityPerformanceDto> findActivityTypePerformance(String teacherId);
    List<StudentSummaryDto> findStudents(String teacherId);

    // RANKING DE UN GRUPO (accesible a los miembros del grupo)
    List<StudentSummaryDto> findGroupRanking(String groupId);
}