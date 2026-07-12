package com.example.repository.activity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.models.activity.Activity;
import com.example.models.activity.ActivityStatus;
import com.example.models.activity.ActivityType;

public class ActivityRepositoryImpl implements ActivityRepository {
    private final Connection connection;

    public ActivityRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    //create
    @Override
    public void create(Activity activity) {

        String sql = """
            INSERT INTO actividad (
                id,
                id_tema,
                id_docente,
                titulo,
                descripcion,
                tipo_actividad,
                puntaje_por_pregunta,
                duracion_minutos,
                estado
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, activity.getId());
            statement.setString(2, activity.getTopicId());
            statement.setString(3, activity.getTeacherId());
            statement.setString(4, activity.getTitle());
            statement.setString(5, activity.getDescription());
            statement.setString(6, activity.getType().name());
            statement.setInt(7, activity.getScorePerQuestion());
            statement.setInt(8, activity.getDurationMinutes());
            statement.setString(9, activity.getStatus().name());

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
                duracion_minutos = ?
            WHERE id = ?
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, activity.getTitle());
            statement.setString(2, activity.getDescription());
            statement.setString(3, activity.getType().name());
            statement.setInt(4, activity.getScorePerQuestion());
            statement.setInt(5, activity.getDurationMinutes());
            statement.setString(6, activity.getId());

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

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, activityId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al publicar la actividad.", e);
        }
    }



    //delete
    @Override
    public void delete(String id) {

        String sql = """
            DELETE FROM actividad
            WHERE id = ?
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

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
                id_docente,
                titulo,
                descripcion,
                tipo_actividad,
                puntaje_por_pregunta,
                duracion_minutos,
                estado,
                created_at
            FROM actividad
            WHERE id = ?
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

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
                id_docente,
                titulo,
                descripcion,
                tipo_actividad,
                puntaje_por_pregunta,
                duracion_minutos,
                estado,
                created_at
            FROM actividad
            WHERE id_tema = ?
            ORDER BY created_at DESC
            """;

        List<Activity> activities = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, topicId);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                activities.add(mapActivity(rs));
            }

            return activities;

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener las actividades.", e);
        }
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

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

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

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, activityId);

            ResultSet rs = statement.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar los intentos de la actividad.", e);
        }
    }


    //mapActivity
    private Activity mapActivity(ResultSet rs) throws SQLException {

        return new Activity(
                rs.getString("id"),
                rs.getString("id_tema"),
                rs.getString("id_docente"),
                rs.getString("titulo"),
                rs.getString("descripcion"),
                ActivityType.valueOf(rs.getString("tipo_actividad")),
                rs.getInt("duracion_minutos"),
                rs.getInt("puntaje_por_pregunta"),
                ActivityStatus.valueOf(rs.getString("estado")),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }

}