package com.example.repository.trivia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.models.trivia.Option;
import com.example.models.trivia.TriviaQuestion;

public class TriviaRepositoryImpl implements TriviaRepository {

    private final Connection connection;

    public TriviaRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int nextQuestionNumber(String activityId) {

        String sql = """
                SELECT COALESCE(MAX(numero_pregunta),0)+1
                FROM actividad_trivia
                WHERE id_actividad = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, activityId);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

            return 1;

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener el número de pregunta.", e);
        }
    }

    
    @Override
    public void create(TriviaQuestion trivia) {

        String sql = """
                INSERT INTO actividad_trivia(
                    id,
                    id_actividad,
                    numero_pregunta,
                    enunciado
                )
                VALUES(?,?,?,?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, trivia.getId());
            statement.setString(2, trivia.getActivityId());
            statement.setInt(3, trivia.getQuestionNumber());
            statement.setString(4, trivia.getStatement());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al crear la pregunta.", e);
        }
    }

    
    @Override
    public void createOption(Option option) {

        String sql = """
                INSERT INTO opcion(
                    id,
                    id_trivia,
                    opcion,
                    correcta
                )
                VALUES(?,?,?,?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, option.getId());
            statement.setString(2, option.getTriviaId());
            statement.setString(3, option.getText());
            statement.setBoolean(4, option.isCorrect());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al crear la opción.", e);
        }
    }


    @Override
    public void update(TriviaQuestion question) {

        String sql = """
                UPDATE actividad_trivia
                SET enunciado = ?
                WHERE id = ?
                """;

        try(PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, question.getStatement());
            ps.setString(2, question.getId());

            ps.executeUpdate();

        }catch(SQLException e) {

            throw new RuntimeException(
                    "Error actualizando pregunta: " + e.getMessage()
            );
        }
    }


    @Override
    public void updateOptions(String triviaId, List<Option> options) {

        String deleteSql = """
                DELETE FROM opcion
                WHERE id_trivia = ?
                """;

        try(PreparedStatement ps = connection.prepareStatement(deleteSql)) {

            ps.setString(1, triviaId);

            ps.executeUpdate();

        }catch(SQLException e) {

            throw new RuntimeException(
                    "Error eliminando opciones anteriores: " + e.getMessage()
            );
        }


        for(Option option : options) {
            createOption(option);
        }
    }


    @Override
    public Optional<TriviaQuestion> findById(String id) {

        String sql = """
                SELECT
                    id,
                    id_actividad,
                    numero_pregunta,
                    enunciado
                FROM actividad_trivia
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {

                TriviaQuestion trivia = mapTrivia(rs);
                trivia.setOptions(findOptions(trivia.getId()));

                return Optional.of(trivia);
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la pregunta.", e);
        }
    }


    @Override
    public List<TriviaQuestion> findAll(String activityId) {

        String sql = """
                SELECT
                    id,
                    id_actividad,
                    numero_pregunta,
                    enunciado
                FROM actividad_trivia
                WHERE id_actividad = ?
                ORDER BY numero_pregunta
                """;

        List<TriviaQuestion> questions = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, activityId);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                TriviaQuestion trivia = mapTrivia(rs);
                trivia.setOptions(findOptions(trivia.getId()));

                questions.add(trivia);
            }

            return questions;

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar preguntas.", e);
        }
    }


    private TriviaQuestion mapTrivia(ResultSet rs) throws SQLException {

    return new TriviaQuestion(
            rs.getString("id"),
            rs.getString("id_actividad"),
            rs.getInt("numero_pregunta"),
            rs.getString("enunciado"));
    }

    private List<Option> findOptions(String triviaId) {

        String sql = """
                SELECT
                    id,
                    id_trivia,
                    opcion,
                    correcta
                FROM opcion
                WHERE id_trivia = ?
                ORDER BY correcta DESC, opcion
                """;

        List<Option> options = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, triviaId);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                options.add(new Option(
                        rs.getString("id"),
                        rs.getString("id_trivia"),
                        rs.getString("opcion"),
                        rs.getBoolean("correcta")));
            }

            return options;

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener las opciones.", e);
        }
    }

    @Override
    public void delete(String id) {

        String sql = """
                DELETE FROM actividad_trivia
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la pregunta.", e);
        }
    }

    @Override
    public void reorderQuestions(String activityId) {

        String selectSql = """
                SELECT id
                FROM actividad_trivia
                WHERE id_actividad = ?
                ORDER BY numero_pregunta
                """;

        String updateSql = """
                UPDATE actividad_trivia
                SET numero_pregunta = ?
                WHERE id = ?
                """;

        try (
            PreparedStatement select = connection.prepareStatement(selectSql);
            PreparedStatement update = connection.prepareStatement(updateSql)
        ) {

            select.setString(1, activityId);

            ResultSet rs = select.executeQuery();

            int number = 1;

            while (rs.next()) {

                update.setInt(1, number++);
                update.setString(2, rs.getString("id"));
                update.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al renumerar las preguntas.", e);
        }
    }
}