package es.dam.di.repository.jdbc;

import es.dam.di.infrastructure.DatabaseConnection;
import es.dam.di.model.Price;
import es.dam.di.model.Videogame;
import es.dam.di.repository.IVideogameRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VideogameRepositoryJdbc implements IVideogameRepository {

    @Override
    public <S extends Videogame> S save(S entity) {
        if (entity == null) {
            throw new IllegalArgumentException("La entidad a guardar no puede ser nula");
        }

        if (entity.getId() == null) {
            // INSERT
            String sql = "INSERT INTO videogames (title, price, release_date, developer_id) VALUES (?, ?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, entity.getTitle());
                stmt.setBigDecimal(2, entity.getPrice() != null ? entity.getPrice().getValue() : null);
                stmt.setString(3, entity.getReleaseDate().toString());
                stmt.setLong(4, entity.getDeveloperId());

                stmt.executeUpdate();

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        entity.setId(generatedKeys.getLong(1));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al insertar el videojuego", e);
            }
        } else {
            // UPDATE
            String sql = "UPDATE videogames SET title = ?, price = ?, release_date = ?, developer_id = ? WHERE id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, entity.getTitle());
                stmt.setBigDecimal(2, entity.getPrice() != null ? entity.getPrice().getValue() : null);
                stmt.setString(3, entity.getReleaseDate().toString());
                stmt.setLong(4, entity.getDeveloperId());
                stmt.setLong(5, entity.getId());

                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Error al actualizar el videojuego", e);
            }
        }
        return entity;
    }

    @Override
    public Videogame findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        String sql = "SELECT * FROM videogames WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return toVideogame(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar videojuego por ID", e);
        }
        return null;
    }

    @Override
    public Optional<Videogame> findByIdOptional(Long id) {
        return Optional.ofNullable(findById(id));
    }

    @Override
    public List<Videogame> findAllToList() {
        List<Videogame> videogames = new ArrayList<>();
        String sql = "SELECT * FROM videogames";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                videogames.add(toVideogame(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar los videojuegos", e);
        }
        return videogames;
    }

    @Override
    public Iterable<Videogame> findAll() {
        return findAllToList();
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        String sql = "DELETE FROM videogames WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el videojuego por ID", e);
        }
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM videogames";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar todos los videojuegos", e);
        }
    }

    @Override
    public boolean existsById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        String sql = "SELECT 1 FROM videogames WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al comprobar la existencia del videojuego", e);
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM videogames";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar los videojuegos", e);
        }
        return 0;
    }

    //Métodos propios

    @Override
    public List<Videogame> findByDeveloperId(Long developerId) {
        if (developerId == null) {
            throw new IllegalArgumentException("El ID del desarrollador no puede ser nulo");
        }
        List<Videogame> videogames = new ArrayList<>();
        String sql = "SELECT * FROM videogames WHERE developer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, developerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    videogames.add(toVideogame(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar videojuegos por ID de desarrollador", e);
        }
        return videogames;
    }

    @Override
    public List<Videogame> findByCategoryId(Long categoryId) {
        if (categoryId == null) {
            throw new IllegalArgumentException("El ID de la categoría no puede ser nulo");
        }
        List<Videogame> videogames = new ArrayList<>();
        String sql = """
            SELECT v.* 
            FROM videogames v
            JOIN videogames_categories vc ON v.id = vc.videogame_id
            WHERE vc.category_id = ?
            """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, categoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    videogames.add(toVideogame(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar videojuegos por ID de categoría", e);
        }
        return videogames;
    }


    private Videogame toVideogame(ResultSet rs) throws SQLException {
        return new Videogame(
                rs.getLong("id"),
                rs.getString("title"),
                new Price(rs.getBigDecimal("price")),
                LocalDate.parse(rs.getString("release_date")),
                rs.getLong("developer_id")
        );
    }
}