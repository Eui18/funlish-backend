package com.example.repository.forum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.models.forum.Forum;

public class ForumRepositoryImpl implements ForumRepository {

    private final Connection connection;

    public ForumRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Forum> findAll(String groupId) {

        String sql = """
                SELECT id, id_grupo AS groupId, id_docente AS teacherId,
                       titulo AS title, descripcion AS description, url, created_at AS createdAt
                FROM foro
                WHERE id_grupo = ?
                ORDER BY created_at DESC
                """;

        List<Forum> forums = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, groupId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    forums.add(mapForum(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar las publicaciones.", e);
        }

        return forums;
    }

    @Override
    public Optional<Forum> findById(String id) {

        String sql = """
                SELECT id, id_grupo AS groupId, id_docente AS teacherId,
                       titulo AS title, descripcion AS description, url, created_at AS createdAt
                FROM foro
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return Optional.of(mapForum(resultSet));
                }

                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar la publicación.", e);
        }
    }

    @Override
    public Forum create(Forum forum) {

        String sql = """
                INSERT INTO foro(id, id_grupo, id_docente, titulo, descripcion, url)
                VALUES(?,?,?,?,?,?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, forum.getId());
            statement.setString(2, forum.getGroupId());
            statement.setString(3, forum.getTeacherId());
            statement.setString(4, forum.getTitle());
            statement.setString(5, forum.getDescription());
            statement.setString(6, forum.getUrl());

            statement.executeUpdate();

            return forum;

        } catch (SQLException e) {
            throw new RuntimeException("Error al crear la publicación.", e);
        }
    }

    @Override
    public boolean delete(String id) {

        String sql = "DELETE FROM foro WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la publicación.", e);
        }
    }

    private Forum mapForum(ResultSet resultSet) throws SQLException {

        Timestamp createdAt = resultSet.getTimestamp("createdAt");

        return new Forum(
                resultSet.getString("id"),
                resultSet.getString("groupId"),
                resultSet.getString("teacherId"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                resultSet.getString("url"),
                createdAt != null ? createdAt.toLocalDateTime() : null
        );
    }
}