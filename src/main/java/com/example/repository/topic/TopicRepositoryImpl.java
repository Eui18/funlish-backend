package com.example.repository.topic;

import java.sql.Connection;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.models.topic.Topic;

public class TopicRepositoryImpl implements TopicRepository {

    private final DataSource dataSource;

    public TopicRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Topic create(Topic topic) {

        String sql = """
                INSERT INTO tema (
                    id,
                    id_unidad,
                    numero,
                    titulo,
                    descripcion
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, topic.getId());
            statement.setString(2, topic.getUnitId());
            statement.setInt(3, topic.getNumber());
            statement.setString(4, topic.getTitle());
            statement.setString(5, topic.getDescription());

            statement.executeUpdate();

            return topic;

        } catch (Exception e) {
            throw new RuntimeException("Error al crear el tema.", e);
        }
    }

    @Override
    public List<Topic> findAll(String unitId) {

        List<Topic> topics = new ArrayList<>();

        String sql = """
                SELECT
                    id,
                    id_unidad AS unitId,
                    numero,
                    titulo,
                    descripcion
                FROM tema
                WHERE id_unidad = ?
                ORDER BY numero
                """;

        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, unitId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    topics.add(mapTopic(resultSet));
                }

                return topics;
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los temas.", e);
        }
    }

    @Override
    public Optional<Topic> findById(String id) {

        String sql = """
                SELECT
                    id,
                    id_unidad AS unitId,
                    numero,
                    titulo,
                    descripcion
                FROM tema
                WHERE id = ?
                """;

        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return Optional.of(mapTopic(resultSet));
                }

                return Optional.empty();
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el tema.", e);
        }
    }

    @Override
    public boolean update(Topic topic) {

        String sql = """
                UPDATE tema
                SET
                    titulo = ?,
                    descripcion = ?
                WHERE id = ?
                """;

        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, topic.getTitle());
            statement.setString(2, topic.getDescription());
            statement.setString(3, topic.getId());

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el tema.", e);
        }
    }

    @Override
    public boolean delete(String id) {

        String sql = "DELETE FROM tema WHERE id = ?";

        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el tema.", e);
        }
    }

    @Override
    public int nextNumber(String unitId) {

        String sql = """
                SELECT COALESCE(MAX(numero), 0) AS max_number
                FROM tema
                WHERE id_unidad = ?
                """;

        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, unitId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("max_number") + 1;
                }
                return 1;
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el número del tema.", e);
        }
    }

    @Override
    public boolean existsTitle(String unitId, String title) {

        String sql = """
                SELECT 1
                FROM tema
                WHERE id_unidad = ?
                AND titulo = ?
                LIMIT 1
                """;

        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, unitId);
            statement.setString(2, title);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al verificar el título del tema.", e);
        }
    }

    private Topic mapTopic(ResultSet resultSet) throws SQLException {
       
        return new Topic(
                resultSet.getString("id"),
                resultSet.getString("unitId"),
                resultSet.getInt("numero"),
                resultSet.getString("titulo"),
                resultSet.getString("descripcion")
        );
    }
}