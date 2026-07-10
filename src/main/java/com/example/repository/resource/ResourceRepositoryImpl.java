package com.example.repository.resource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.models.resource.Resource;
import com.example.models.resource.ResourceType;

public class ResourceRepositoryImpl implements ResourceRepository {

    private final Connection connection;

    public ResourceRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Resource create(Resource resource) {

        String sql = """
                INSERT INTO material (
                    id,
                    id_tema,
                    nombre,
                    tipo,
                    descripcion,
                    url
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, resource.getId());
            statement.setString(2, resource.getTopicId());
            statement.setString(3, resource.getName());
            statement.setString(4, resource.getType().name());
            statement.setString(5, resource.getDescription());
            statement.setString(6, resource.getUrl());

            statement.executeUpdate();

            return resource;

        } catch (SQLException e) {
            throw new RuntimeException("Error al crear recurso.", e);
        }
    }

    @Override
    public List<Resource> findAll(String topicId) {

        List<Resource> resources = new ArrayList<>();

        String sql = """
                SELECT
                    id,
                    id_tema,
                    nombre,
                    tipo,
                    descripcion,
                    url
                FROM material
                WHERE id_tema = ?
                ORDER BY nombre
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, topicId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    resources.add(mapResource(resultSet));
                }
            }

            return resources;

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener recursos.", e);
        }
    }

    @Override
    public Optional<Resource> findById(String id) {

        String sql = """
                SELECT
                    id,
                    id_tema,
                    nombre,
                    tipo,
                    descripcion,
                    url
                FROM material
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return Optional.of(mapResource(resultSet));
                }

                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar recurso.", e);
        }
    }

    @Override
    public boolean update(Resource resource) {

        String sql = """
                UPDATE material
                SET
                    nombre = ?,
                    tipo = ?,
                    descripcion = ?,
                    url = ?
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, resource.getName());
            statement.setString(2, resource.getType().name());
            statement.setString(3, resource.getDescription());
            statement.setString(4, resource.getUrl());
            statement.setString(5, resource.getId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar recurso.", e);
        }
    }

    @Override
    public boolean delete(String id) {

        String sql = "DELETE FROM material WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar recurso.", e);
        }
    }

    @Override
    public boolean existsByName(String topicId, String name) {

        String sql = """
                SELECT 1
                FROM material
                WHERE id_tema = ?
                AND nombre = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, topicId);
            statement.setString(2, name);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar recurso.", e);
        }
    }

    private Resource mapResource(ResultSet resultSet) throws SQLException {

        return new Resource(
                resultSet.getString("id"),
                resultSet.getString("id_tema"),
                resultSet.getString("nombre"),
                ResourceType.valueOf(resultSet.getString("tipo")),
                resultSet.getString("descripcion"),
                resultSet.getString("url")
        );
    }
}