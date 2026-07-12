package com.example.repository.scramble;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.models.scramble.Scramble;
import com.example.models.scramble.ScrambleType;

public class ScrambleRepositoryImpl implements ScrambleRepository {

    private final Connection connection;

    public ScrambleRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Scramble scramble) {

        String sql = """
                INSERT INTO actividad_scramble
                (
                    id,
                    id_actividad,
                    numero_reto,
                    contenido_correcto,
                    tipo
                )
                VALUES (?, ?, ?, ?, ?)
                """;
        try ( PreparedStatement ps = connection.prepareStatement(sql) ) {

            ps.setString(1, scramble.getId());
            ps.setString(2, scramble.getActivityId());
            ps.setInt(3, scramble.getNumberChallenge());
            ps.setString(4, scramble.getCorrectContent());
            ps.setString(5, scramble.getType().name());

            ps.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException("Error al crear scramble: " + e.getMessage()
            );
        }
    }



    @Override
    public List<Scramble> findAll(String activityId) {

        List<Scramble> scrambles = new ArrayList<>();

        String sql = """
                SELECT
                    id,
                    id_actividad,
                    numero_reto,
                    contenido_correcto,
                    tipo
                FROM actividad_scramble
                WHERE id_actividad = ?
                ORDER BY numero_reto
                """;
        try ( PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, activityId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {

                scrambles.add( new Scramble(
                        rs.getString("id"),
                        rs.getString("id_actividad"),
                        rs.getInt("numero_reto"),
                        rs.getString("contenido_correcto"),
                        ScrambleType.valueOf(
                            rs.getString("tipo")
                        )
                    )
                );
            }
        } catch(SQLException e) {

            throw new RuntimeException(
                    "Error al obtener scrambles: "
                    + e.getMessage()
            );
        }
        return scrambles;
    }



    @Override
    public Optional<Scramble> findById(String id) {

        String sql = """
                SELECT
                    id,
                    id_actividad,
                    numero_reto,
                    contenido_correcto,
                    tipo
                FROM actividad_scramble
                WHERE id = ?
                """;
        try( PreparedStatement ps = connection.prepareStatement(sql) ) {
            ps.setString(1,id);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {

                return Optional.of(
                    new Scramble(
                        rs.getString("id"),
                        rs.getString("id_actividad"),
                        rs.getInt("numero_reto"),
                        rs.getString("contenido_correcto"),
                        ScrambleType.valueOf(
                            rs.getString("tipo")
                        )
                    )
                );
            }
        }catch(SQLException e){

            throw new RuntimeException(
                "Error al buscar scramble: "
                + e.getMessage()
            );
        }
        return Optional.empty();
    }


    @Override
    public boolean delete(String id) {

        String sql =
                "DELETE FROM actividad_scramble WHERE id = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql)
        ){
            ps.setString(1,id);

            return ps.executeUpdate() > 0;

        }catch(SQLException e){

            throw new RuntimeException( "Error al eliminar scramble: " + e.getMessage()
            );
        }
    }



    @Override
    public boolean existsByContent(String activityId, String content ) {

        String sql = """
                SELECT COUNT(*)
                FROM actividad_scramble
                WHERE id_actividad = ?
                AND contenido_correcto = ?
                """;
        try( PreparedStatement ps = connection.prepareStatement(sql) ){

            ps.setString(1, activityId);
            ps.setString(2, content);

            ResultSet rs = ps.executeQuery();

            rs.next();

            return rs.getInt(1) > 0;

        }catch(SQLException e){

            throw new RuntimeException( "Error verificando scramble: " + e.getMessage()
            );
        }
    }



    @Override
    public int nextNumberChallenge(String activityId) {

        String sql = """
                SELECT COALESCE(MAX(numero_reto),0)+1
                FROM actividad_scramble
                WHERE id_actividad = ?
                """;
        try(PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setString(1, activityId);

            ResultSet rs = ps.executeQuery();

            rs.next();

            return rs.getInt(1);
        }catch(SQLException e){

            throw new RuntimeException("Error generando número de reto: " + e.getMessage()
            );
        }
    }
}