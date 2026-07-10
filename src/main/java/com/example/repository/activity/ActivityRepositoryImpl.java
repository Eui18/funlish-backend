package com.example.repository.activity;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.models.activity.Activity;
import com.example.models.activity.ActivityType;

public class ActivityRepositoryImpl implements ActivityRepository {

    private final Connection connection;

    public ActivityRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Activity create(Activity activity) {

        String sql = """
                INSERT INTO actividad (
                    id,
                    id_tema,
                    id_docente,
                    titulo,
                    descripcion,
                    tipo_actividad,
                    puntaje_maximo,
                    duracion_minutos,
                    fecha_inicio,
                    fecha_final,
                    hora_inicio,
                    hora_final
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, activity.getId());
            statement.setString(2, activity.getTopicId());
            statement.setString(3, activity.getTeacherId());
            statement.setString(4, activity.getTitle());
            statement.setString(5, activity.getDescription());
            statement.setString(6, activity.getType().name());
            statement.setInt(7, activity.getMaxScore());
            statement.setInt(8, activity.getDurationMinutes());
            statement.setDate(9, Date.valueOf(activity.getStartDate()));
            statement.setDate(10, Date.valueOf(activity.getEndDate()));
            statement.setTime(11, Time.valueOf(activity.getStartTime()));
            statement.setTime(12, Time.valueOf(activity.getEndTime()));

            statement.executeUpdate();

            return activity;

        } catch (SQLException e) {
            throw new RuntimeException("Error al crear la actividad.", e);
        }
    }

    @Override
    public List<Activity> findAll(String topicId) {

        List<Activity> activities = new ArrayList<>();

        String sql = """
                SELECT
                    id,
                    id_tema AS topicId,
                    id_docente AS teacherId,
                    titulo AS title,
                    descripcion AS description,
                    tipo_actividad AS type,
                    puntaje_maximo AS maxScore,
                    duracion_minutos AS durationMinutes,
                    fecha_inicio AS startDate,
                    fecha_final AS endDate,
                    hora_inicio AS startTime,
                    hora_final AS endTime
                FROM actividad
                WHERE id_tema = ?
                ORDER BY created_at DESC
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, topicId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    activities.add(mapActivity(resultSet));
                }
            }

            return activities;

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener actividades.", e);
        }
    }

    @Override
    public Optional<Activity> findById(String id) {

        String sql = """
                SELECT
                    id,
                    id_tema AS topicId,
                    id_docente AS teacherId,
                    titulo AS title,
                    descripcion AS description,
                    tipo_actividad AS type,
                    puntaje_maximo AS maxScore,
                    duracion_minutos AS durationMinutes,
                    fecha_inicio AS startDate,
                    fecha_final AS endDate,
                    hora_inicio AS startTime,
                    hora_final AS endTime
                FROM actividad
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return Optional.of(mapActivity(resultSet));
                }

                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar la actividad.", e);
        }
    }

    @Override
    public boolean update(Activity activity) {

        String sql = """
                UPDATE actividad
                SET
                    titulo = ?,
                    descripcion = ?,
                    tipo_actividad = ?,
                    puntaje_maximo = ?,
                    duracion_minutos = ?,
                    fecha_inicio = ?,
                    fecha_final = ?,
                    hora_inicio = ?,
                    hora_final = ?
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, activity.getTitle());
            statement.setString(2, activity.getDescription());
            statement.setString(3, activity.getType().name());
            statement.setInt(4, activity.getMaxScore());
            statement.setInt(5, activity.getDurationMinutes());
            statement.setDate(6, Date.valueOf(activity.getStartDate()));
            statement.setDate(7, Date.valueOf(activity.getEndDate()));
            statement.setTime(8, Time.valueOf(activity.getStartTime()));
            statement.setTime(9, Time.valueOf(activity.getEndTime()));
            statement.setString(10, activity.getId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la actividad.", e);
        }
    }

    @Override
    public boolean delete(String id) {

        String sql = """
                DELETE FROM actividad
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la actividad.", e);
        }
    }

    @Override
    public boolean existsByTitle(String topicId, String title) {

        String sql = """
                SELECT 1
                FROM actividad
                WHERE id_tema = ?
                AND titulo = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, topicId);
            statement.setString(2, title);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al validar el título de la actividad.", e);
        }
    }

    @Override
    public boolean existsByTitle(String topicId, String title, String activityId) {

        String sql = """
                SELECT 1
                FROM actividad
                WHERE id_tema = ?
                AND titulo = ?
                AND id <> ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, topicId);
            statement.setString(2, title);
            statement.setString(3, activityId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al validar el título de la actividad.", e);
        }
    }

    @Override
    public boolean hasStudentAttempts(String activityId) {

        String sql = """
                SELECT 1
                FROM actividad_alumno
                WHERE id_actividad = ?
                AND fecha_inicio IS NOT NULL
                LIMIT 1
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, activityId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al validar intentos de alumnos.", e);
        }
    }

    private Activity mapActivity(ResultSet resultSet) throws SQLException {

        return new Activity(
                resultSet.getString("id"),
                resultSet.getString("id_tema"),
                resultSet.getString("id_docente"),
                resultSet.getString("titulo"),
                resultSet.getString("descripcion"),
                ActivityType.valueOf(resultSet.getString("tipo_actividad")),
                resultSet.getInt("puntaje_maximo"),
                resultSet.getInt("duracion_minutos"),
                resultSet.getDate("fecha_inicio").toLocalDate(),
                resultSet.getDate("fecha_final").toLocalDate(),
                resultSet.getTime("hora_inicio").toLocalTime(),
                resultSet.getTime("hora_final").toLocalTime()
        );
    }
}