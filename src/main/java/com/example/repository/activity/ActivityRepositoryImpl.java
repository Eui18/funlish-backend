package com.example.repository.activity;

import java.sql.Connection;
import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.models.activity.Activity;
import com.example.models.activity.ActivityStatus;
import com.example.models.activity.ActivityType;

public class ActivityRepositoryImpl implements ActivityRepository {
    private final DataSource dataSource;

    public ActivityRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    //create
    @Override
    public void create(Activity activity) {

        String sql = """
            INSERT INTO actividad (
                id,
                id_tema,
                titulo,
                descripcion,
                tipo_actividad,
                puntaje_por_pregunta,
                duracion_minutos,
                estado,
                fecha_inicio,
                fecha_final,
                hora_inicio,
                hora_final
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, activity.getId());
            statement.setString(2, activity.getTopicId());
            statement.setString(3, activity.getTitle());
            statement.setString(4, activity.getDescription());
            statement.setString(5, activity.getType().name());
            statement.setInt(6, activity.getScorePerQuestion());
            statement.setInt(7, activity.getDurationMinutes());
            statement.setString(8, activity.getStatus().name());
            setDate(statement, 9, activity.getStartDate() != null ? Date.valueOf(activity.getStartDate()) : null);
            setDate(statement, 10, activity.getEndDate() != null ? Date.valueOf(activity.getEndDate()) : null);
            setTime(statement, 11, activity.getStartTime() != null ? Time.valueOf(activity.getStartTime()) : null);
            setTime(statement, 12, activity.getEndTime() != null ? Time.valueOf(activity.getEndTime()) : null);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al crear la actividad.", e);
        }
    }



    //update
    @Override
    public void update(Activity activity) {

        String sql = """
            UPDATE actividad
            SET
                titulo = ?,
                descripcion = ?,
                tipo_actividad = ?,
                puntaje_por_pregunta = ?,
                duracion_minutos = ?,
                fecha_inicio = ?,
                fecha_final = ?,
                hora_inicio = ?,
                hora_final = ?
            WHERE id = ?
            """;

        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, activity.getTitle());
            statement.setString(2, activity.getDescription());
            statement.setString(3, activity.getType().name());
            statement.setInt(4, activity.getScorePerQuestion());
            statement.setInt(5, activity.getDurationMinutes());
            setDate(statement, 6, activity.getStartDate() != null ? Date.valueOf(activity.getStartDate()) : null);
            setDate(statement, 7, activity.getEndDate() != null ? Date.valueOf(activity.getEndDate()) : null);
            setTime(statement, 8, activity.getStartTime() != null ? Time.valueOf(activity.getStartTime()) : null);
            setTime(statement, 9, activity.getEndTime() != null ? Time.valueOf(activity.getEndTime()) : null);
            statement.setString(10, activity.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la actividad.", e);
        }
    }



    //publish
    @Override
    public void publish(String activityId) {

        String sql = """
            UPDATE actividad
            SET estado = 'PUBLISHED'
            WHERE id = ?
            """;

        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, activityId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al publicar la actividad.", e);
        }
    }



    //finish
    @Override
    public void finish(String activityId) {

        String sql = """
            UPDATE actividad
            SET estado = 'FINISHED'
            WHERE id = ?
            """;

        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, activityId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al finalizar la actividad.", e);
        }
    }



    //delete
    @Override
    public void delete(String id) {

        String sql = """
            DELETE FROM actividad
            WHERE id = ?
            """;

        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la actividad.", e);
        }
    }

    //findById
    @Override
    public Optional<Activity> findById(String id) {

        String sql = """
            SELECT
                id,
                id_tema,
                titulo,
                descripcion,
                tipo_actividad,
                puntaje_por_pregunta,
                duracion_minutos,
                estado,
                created_at,
                fecha_inicio,
                fecha_final,
                hora_inicio,
                hora_final
            FROM actividad
            WHERE id = ?
            """;

        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return Optional.of(mapActivity(rs));
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la actividad.", e);
        }
    }


    //findAll
    @Override
    public List<Activity> findAll(String topicId) {

        String sql = """
            SELECT
                id,
                id_tema,
                titulo,
                descripcion,
                tipo_actividad,
                puntaje_por_pregunta,
                duracion_minutos,
                estado,
                created_at,
                fecha_inicio,
                fecha_final,
                hora_inicio,
                hora_final
            FROM actividad
            WHERE id_tema = ?
            ORDER BY created_at DESC
            """;

        List<Activity> activities = new ArrayList<>();

        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, topicId);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                activities.add(mapActivity(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener las actividades.", e);
        }

        return activities;
    }



    //existByTitle
    @Override
    public boolean existsByTitle(String topicId, String title) {

        String sql = """
            SELECT 1
            FROM actividad
            WHERE id_tema = ?
            AND titulo = ?
            LIMIT 1
            """;

        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, topicId);
            statement.setString(2, title);

            ResultSet rs = statement.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar el título.", e);
        }
    }



    //hasStudentAttempts
    @Override
    public boolean hasStudentAttempts(String activityId) {

        String sql = """
            SELECT 1
            FROM actividad_alumno
            WHERE id_actividad = ?
            LIMIT 1
            """;

        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, activityId);

            ResultSet rs = statement.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar los intentos de la actividad.", e);
        }
    }


    // Si ya pasó fecha_final + hora_final y sigue PUBLISHED, la considera vencida
    @Override
    public List<Activity> findExpiredPublished() {

        String sql = """
            SELECT
                id,
                id_tema,
                titulo,
                descripcion,
                tipo_actividad,
                puntaje_por_pregunta,
                duracion_minutos,
                estado,
                created_at,
                fecha_inicio,
                fecha_final,
                hora_inicio,
                hora_final
            FROM actividad
            WHERE estado = 'PUBLISHED'
            AND fecha_final IS NOT NULL
            AND hora_final IS NOT NULL
            AND TIMESTAMP(fecha_final, hora_final) <= NOW()
            """;

        List<Activity> activities = new ArrayList<>();

        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                activities.add(mapActivity(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar actividades vencidas.", e);
        }

        return activities;
    }


    private void setDate(PreparedStatement statement, int index, Date value) throws SQLException {

        if (value == null) {
            statement.setNull(index, Types.DATE);
        } else {
            statement.setDate(index, value);
        }
    }


    private void setTime(PreparedStatement statement, int index, Time value) throws SQLException {

        if (value == null) {
            statement.setNull(index, Types.TIME);
        } else {
            statement.setTime(index, value);
        }
    }


    //mapActivity
    private Activity mapActivity(ResultSet rs) throws SQLException {

        Date startDate = rs.getDate("fecha_inicio");
        Date endDate = rs.getDate("fecha_final");
        Time startTime = rs.getTime("hora_inicio");
        Time endTime = rs.getTime("hora_final");

        return new Activity(
                rs.getString("id"),
                rs.getString("id_tema"),
                rs.getString("titulo"),
                rs.getString("descripcion"),
                ActivityType.valueOf(rs.getString("tipo_actividad")),
                rs.getInt("duracion_minutos"),
                rs.getInt("puntaje_por_pregunta"),
                ActivityStatus.valueOf(rs.getString("estado")),
                rs.getTimestamp("created_at").toLocalDateTime(),
                startDate != null ? startDate.toLocalDate() : null,
                endDate != null ? endDate.toLocalDate() : null,
                startTime != null ? startTime.toLocalTime() : null,
                endTime != null ? endTime.toLocalTime() : null
        );
    }

}