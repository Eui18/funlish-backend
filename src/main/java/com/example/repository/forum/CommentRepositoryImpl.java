package com.example.repository.forum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.models.forum.Comment;

public class CommentRepositoryImpl implements CommentRepository {

    private final Connection connection;

    public CommentRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Comment> findAllByForum(String forumId) {

        String sql = """
                SELECT id, id_foro AS forumId, id_usuario AS userId,
                       comentario AS comment, created_at AS createdAt
                FROM comentario
                WHERE id_foro = ?
                ORDER BY created_at ASC
                """;

        List<Comment> comments = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, forumId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    comments.add(mapComment(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar los comentarios.", e);
        }

        return comments;
    }

    @Override
    public Optional<Comment> findById(String id) {

        String sql = """
                SELECT id, id_foro AS forumId, id_usuario AS userId,
                       comentario AS comment, created_at AS createdAt
                FROM comentario
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return Optional.of(mapComment(resultSet));
                }

                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el comentario.", e);
        }
    }

    @Override
    public Comment create(Comment comment) {

        String sql = """
                INSERT INTO comentario(id, id_foro, id_usuario, comentario)
                VALUES(?,?,?,?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, comment.getId());
            statement.setString(2, comment.getForumId());
            statement.setString(3, comment.getUserId());
            statement.setString(4, comment.getComment());

            statement.executeUpdate();

            return comment;

        } catch (SQLException e) {
            throw new RuntimeException("Error al crear el comentario.", e);
        }
    }

    @Override
    public boolean delete(String id) {

        String sql = "DELETE FROM comentario WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el comentario.", e);
        }
    }

    private Comment mapComment(ResultSet resultSet) throws SQLException {

        Timestamp createdAt = resultSet.getTimestamp("createdAt");

        return new Comment(
                resultSet.getString("id"),
                resultSet.getString("forumId"),
                resultSet.getString("userId"),
                resultSet.getString("comment"),
                createdAt != null ? createdAt.toLocalDateTime() : null
        );
    }
}