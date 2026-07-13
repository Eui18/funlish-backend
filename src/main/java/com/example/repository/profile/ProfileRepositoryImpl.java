package com.example.repository.profile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.dtos.profile.ActivityPerformanceDto;
import com.example.dtos.profile.StudentProfileDto;
import com.example.dtos.profile.StudentRankingDto;
import com.example.dtos.profile.StudentStatisticsDto;
import com.example.dtos.profile.StudentSummaryDto;
import com.example.dtos.profile.TeacherDashboardDto;
import com.example.dtos.profile.ThemePerformanceDto;

public class ProfileRepositoryImpl implements ProfileRepository {

    private final Connection connection;

    public ProfileRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<StudentProfileDto> findStudentProfile(String studentId) {

        String sql = """
            SELECT id, nombre, correo, matricula
            FROM usuario
            WHERE id = ?
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, studentId);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            }

            return Optional.of(new StudentProfileDto(
                rs.getString("id"),
                rs.getString("nombre"),
                rs.getString("correo"),
                rs.getString("matricula")
            ));

        } catch (SQLException e) {
            throw new RuntimeException(
                "Error obteniendo perfil del alumno.",
                e
            );
        }
    }

    @Override
    public StudentStatisticsDto findStudentStatistics(String studentId) {

        String sql = """
            SELECT
                COALESCE(SUM(aa.puntaje + aa.bonus_puntos), 0) AS puntos,
                SUM(CASE WHEN aa.estado = 'COMPLETADA' THEN 1 ELSE 0 END) AS completadas,
                SUM(CASE WHEN aa.estado <> 'COMPLETADA' THEN 1 ELSE 0 END) AS pendientes
            FROM actividad_alumno aa
            WHERE aa.id_alumno = ?
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, studentId);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return new StudentStatisticsDto(
                    java.math.BigDecimal.ZERO,
                    0,
                    0
                );
            }

            return new StudentStatisticsDto(
                rs.getBigDecimal("puntos"),
                rs.getInt("completadas"),
                rs.getInt("pendientes")
            );

        } catch (SQLException e) {
            throw new RuntimeException(
                "Error obteniendo estadísticas del alumno.",
                e
            );
        }
    }

    @Override
    public StudentRankingDto findStudentRanking(String studentId) {

        String sql = """
            SELECT posicion, total_alumnos
            FROM (
                SELECT
                    u.id,
                    RANK() OVER(
                        PARTITION BY u.id_grupo
                        ORDER BY COALESCE(SUM(aa.puntaje + aa.bonus_puntos), 0) DESC
                    ) AS posicion,
                    COUNT(*) OVER(
                        PARTITION BY u.id_grupo
                    ) AS total_alumnos
                FROM usuario u
                LEFT JOIN actividad_alumno aa
                    ON aa.id_alumno = u.id
                WHERE u.rol = 'STUDENT'
                GROUP BY u.id, u.id_grupo
            ) ranking
            WHERE id = ?
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, studentId);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return new StudentRankingDto(0, 0);
            }

            return new StudentRankingDto(
                rs.getInt("posicion"),
                rs.getInt("total_alumnos")
            );

        } catch (SQLException e) {
            throw new RuntimeException(
                "Error obteniendo ranking del alumno.",
                e
            );
        }
    }


    @Override
    public List<ThemePerformanceDto> findThemePerformance(String studentId) {

        String sql = """
            SELECT
                t.titulo,
                ROUND(AVG(
                    ((aa.puntaje + aa.bonus_puntos) /
                    (
                        a.puntaje_por_pregunta *
                        CASE
                            WHEN a.tipo_actividad = 'TRIVIA'
                            THEN (SELECT COUNT(*) FROM actividad_trivia at WHERE at.id_actividad = a.id)
                            ELSE (SELECT COUNT(*) FROM actividad_scramble ac WHERE ac.id_actividad = a.id)
                        END
                    )) * 100
                ), 2) AS porcentaje

            FROM actividad_alumno aa
            JOIN actividad a ON a.id = aa.id_actividad
            JOIN tema t ON t.id = a.id_tema

            WHERE aa.id_alumno = ?
            AND aa.estado = 'COMPLETADA'

            GROUP BY t.id, t.titulo
            ORDER BY porcentaje DESC
            """;

        List<ThemePerformanceDto> themes = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, studentId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                themes.add(
                    new ThemePerformanceDto(
                        rs.getString("titulo"),
                        rs.getDouble("porcentaje")
                    )
                );
            }

            return themes;

        } catch(SQLException e) {
            throw new RuntimeException(
                "Error obteniendo rendimiento por tema.",
                e
            );
        }
    }


    @Override
    public List<ActivityPerformanceDto> findActivityPerformance(String studentId) {

        String sql = """
            SELECT
                a.titulo,
                a.tipo_actividad,
                ROUND(
                    ((aa.puntaje + aa.bonus_puntos) /
                    (a.puntaje_por_pregunta *
                    CASE
                        WHEN a.tipo_actividad = 'TRIVIA'
                        THEN (SELECT COUNT(*) FROM actividad_trivia at WHERE at.id_actividad = a.id)
                        ELSE (SELECT COUNT(*) FROM actividad_scramble ac WHERE ac.id_actividad = a.id)
                    END
                    )) * 100,
                2) AS porcentaje

            FROM actividad_alumno aa
            JOIN actividad a ON a.id = aa.id_actividad

            WHERE aa.id_alumno = ?
            AND aa.estado = 'COMPLETADA'

            ORDER BY porcentaje ASC
            LIMIT 10
            """;

        List<ActivityPerformanceDto> activities = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, studentId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                activities.add(
                    new ActivityPerformanceDto(
                        rs.getString("titulo"),
                        rs.getString("tipo_actividad"),
                        rs.getDouble("porcentaje")
                    )
                );
            }

            return activities;

        } catch(SQLException e) {
            throw new RuntimeException(
                "Error obteniendo rendimiento por actividad.",
                e
            );
        }
    }


    // DASHBOARD DOCENTE
    @Override
    public TeacherDashboardDto findTeacherDashboard(String teacherId) {

        String sql = """
            SELECT
                COALESCE(AVG(
                    (aa.puntaje + aa.bonus_puntos) /
                    (
                        a.puntaje_por_pregunta *
                        CASE
                            WHEN a.tipo_actividad = 'TRIVIA'
                            THEN (SELECT COUNT(*) FROM actividad_trivia at WHERE at.id_actividad = a.id)
                            ELSE (SELECT COUNT(*) FROM actividad_scramble ac WHERE ac.id_actividad = a.id)
                        END
                    ) * 100
                ), 0) AS promedio,

                COUNT(DISTINCT u.id) AS alumnos,

                SUM(CASE WHEN aa.estado = 'COMPLETADA' THEN 1 ELSE 0 END) AS completadas,

                SUM(CASE WHEN aa.estado != 'COMPLETADA' THEN 1 ELSE 0 END) AS pendientes

            FROM grupo g
            JOIN usuario u ON u.id_grupo = g.id
            LEFT JOIN actividad_alumno aa ON aa.id_alumno = u.id
            LEFT JOIN actividad a ON a.id = aa.id_actividad
            WHERE g.id_docente = ?
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, teacherId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new TeacherDashboardDto(
                    rs.getDouble("promedio"),
                    rs.getInt("alumnos"),
                    rs.getInt("completadas"),
                    rs.getInt("pendientes")
                );
            }

            return new TeacherDashboardDto(0, 0, 0, 0);

        } catch (SQLException e) {
            throw new RuntimeException(
                "Error obteniendo dashboard docente.",
                e
            );
        }
    }



    @Override
    public List<ThemePerformanceDto> findWorstThemes(String teacherId) {

        String sql = """
            SELECT
                t.titulo,
                ROUND(AVG(
                    (
                        (aa.puntaje + aa.bonus_puntos) /
                        (
                            a.puntaje_por_pregunta *
                            CASE
                                WHEN a.tipo_actividad = 'TRIVIA'
                                THEN (SELECT COUNT(*) FROM actividad_trivia at WHERE at.id_actividad = a.id)
                                ELSE (SELECT COUNT(*) FROM actividad_scramble ac WHERE ac.id_actividad = a.id)
                            END
                        )
                    ) * 100
                ), 2) AS porcentaje

            FROM actividad_alumno aa
            JOIN actividad a ON a.id = aa.id_actividad
            JOIN tema t ON t.id = a.id_tema

            WHERE a.id_docente = ?
            AND aa.estado = 'COMPLETADA'

            GROUP BY t.id, t.titulo
            ORDER BY porcentaje ASC
            LIMIT 5
            """;

        List<ThemePerformanceDto> themes = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, teacherId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                themes.add(
                    new ThemePerformanceDto(
                        rs.getString("titulo"),
                        rs.getDouble("porcentaje")
                    )
                );
            }

            return themes;

        } catch (SQLException e) {
            throw new RuntimeException(
                "Error obteniendo temas con bajo rendimiento.",
                e
            );
        }
    }



    @Override
    public List<ActivityPerformanceDto> findActivityTypePerformance(String teacherId) {

        String sql = """
            SELECT
                a.tipo_actividad,
                ROUND(AVG(
                    (
                        (aa.puntaje + aa.bonus_puntos) /
                        (
                            a.puntaje_por_pregunta *
                            CASE
                                WHEN a.tipo_actividad = 'TRIVIA'
                                THEN (SELECT COUNT(*) FROM actividad_trivia at WHERE at.id_actividad = a.id)
                                ELSE (SELECT COUNT(*) FROM actividad_scramble ac WHERE ac.id_actividad = a.id)
                            END
                        )
                    ) * 100
                ), 2) AS porcentaje

            FROM actividad_alumno aa
            JOIN actividad a ON a.id = aa.id_actividad

            WHERE a.id_docente = ?
            AND aa.estado = 'COMPLETADA'

            GROUP BY a.tipo_actividad
            """;

        List<ActivityPerformanceDto> activities = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, teacherId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                activities.add(
                    new ActivityPerformanceDto(
                        "Rendimiento " + rs.getString("tipo_actividad"),
                        rs.getString("tipo_actividad"),
                        rs.getDouble("porcentaje")
                    )
                );
            }

            return activities;

        } catch (SQLException e) {
            throw new RuntimeException(
                "Error obteniendo rendimiento por tipo.",
                e
            );
        }
    }



    @Override
    public List<StudentSummaryDto> findStudents(String teacherId) {

        String sql = """
            SELECT
                u.id,
                u.nombre,
                u.matricula,
                COALESCE(SUM(aa.puntaje + aa.bonus_puntos), 0) AS puntos

            FROM usuario u
            JOIN grupo g ON g.id = u.id_grupo
            LEFT JOIN actividad_alumno aa ON aa.id_alumno = u.id

            WHERE g.id_docente = ?
            AND u.rol = 'STUDENT'

            GROUP BY u.id, u.nombre, u.matricula
            ORDER BY puntos DESC
            """;

        List<StudentSummaryDto> students = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, teacherId);

            ResultSet rs = ps.executeQuery();

            int position = 1;

            while (rs.next()) {
                students.add(
                    new StudentSummaryDto(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("matricula"),
                        rs.getBigDecimal("puntos"),
                        position++
                    )
                );
            }

            return students;

        } catch (SQLException e) {
            throw new RuntimeException(
                "Error obteniendo alumnos.",
                e
            );
        }
    }

}