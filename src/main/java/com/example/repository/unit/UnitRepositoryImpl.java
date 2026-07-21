package com.example.repository.unit;

import java.sql.Connection;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.models.unit.Unit;

public class UnitRepositoryImpl implements UnitRepository {

    private final DataSource dataSource;

    public UnitRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Unit> findAll(String groupId) {
        List<Unit> units = new ArrayList<>();
        String sql = """
                SELECT id, id_grupo AS groupId, nombre AS name, numero AS number
                FROM unidad
                WHERE id_grupo = ?
                ORDER BY numero
                """;
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, groupId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    units.add(mapUnit(resultSet));
                }
            }
            return units;
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener las unidades.", e);
        }
    }

    @Override
    public Optional<Unit> findById(String id) {
        String sql = """
                SELECT id, id_grupo AS groupId, nombre AS name, numero AS number
                FROM unidad
                WHERE id = ?
                """;
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapUnit(resultSet));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar la unidad por ID.", e);
        }
    }

    @Override
    public Unit create(Unit unit) {
        String sql = """
                INSERT INTO unidad(id, id_grupo, nombre, numero)
                VALUES (?, ?, ?, ?)
                """;
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, unit.getId());
            statement.setString(2, unit.getGroupId());
            statement.setString(3, unit.getName());
            statement.setInt(4, unit.getNumber());

            statement.executeUpdate();
            return unit;
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear la unidad en la base de datos.", e);
        }
    }

    @Override
    public boolean update(Unit unit) {
        String sql = """
                UPDATE unidad
                SET nombre = ?, numero = ?
                WHERE id = ?
                """;
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, unit.getName());
            statement.setInt(2, unit.getNumber());
            statement.setString(3, unit.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la unidad.", e);
        }
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM unidad WHERE id = ?";
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la unidad.", e);
        }
    }

    @Override
    public Optional<Unit> findByName(String name, String groupId) {
        String sql = """
                SELECT id, id_grupo AS groupId, nombre AS name, numero AS number
                FROM unidad
                WHERE nombre = ? AND id_grupo = ?
                """;
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, groupId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapUnit(resultSet));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar la unidad por nombre.", e);
        }
    }

    @Override
    public int nextNumber(String groupId) {
        String sql = """
                SELECT COALESCE(MAX(numero), 0) AS max_number
                FROM unidad
                WHERE id_grupo = ?
                """;
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, groupId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("max_number") + 1;
                }
                return 1;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al generar el número de la unidad.", e);
        }
    }

    private Unit mapUnit(ResultSet resultSet) throws SQLException {
        return new Unit(
                resultSet.getString("id"),
                resultSet.getString("groupId"),
                resultSet.getString("name"),
                resultSet.getInt("number")
        );
    }
}