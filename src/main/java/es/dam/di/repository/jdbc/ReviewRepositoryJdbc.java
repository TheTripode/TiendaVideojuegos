package es.dam.di.repository.jdbc;

import es.dam.di.infrastructure.DatabaseConnection;
import es.dam.di.model.Review;
import es.dam.di.repository.IReviewRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReviewRepositoryJdbc implements IReviewRepository {

    @Override
    public <S extends Review> S save(S entity) {
        if (entity == null) {
            throw new IllegalArgumentException("La entidad a guardar no puede ser nula");
        }

        if (entity.getId() == null) {
            // INSERT
            String sql = "INSERT INTO reviews (account_id, videogame_id, recommended, comment, publication_date) VALUES (?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setLong(1, entity.getAccountId());
                stmt.setLong(2, entity.getVideogameId());
                stmt.setBoolean(3, entity.isRecommended());
                stmt.setString(4, entity.getComment());
                stmt.setString(5, entity.getPublicationDate().toString());

                stmt.executeUpdate();

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        entity.setId(generatedKeys.getLong(1));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al insertar la reseña", e);
            }
        } else {
            // UPDATE
            String sql = "UPDATE reviews SET account_id = ?, videogame_id = ?, recommended = ?, comment = ?, publication_date = ? WHERE id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setLong(1, entity.getAccountId());
                stmt.setLong(2, entity.getVideogameId());
                stmt.setBoolean(3, entity.isRecommended());
                stmt.setString(4, entity.getComment());
                stmt.setString(5, entity.getPublicationDate().toString());
                stmt.setLong(6, entity.getId());

                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Error al actualizar la reseña", e);
            }
        }
        return entity;
    }

    @Override
    public Review findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        String sql = "SELECT * FROM reviews WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return toReview(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar reseña por ID", e);
        }
        return null;
    }

    @Override
    public Optional<Review> findByIdOptional(Long id) {
        return Optional.ofNullable(findById(id));
    }

    @Override
    public List<Review> findAllToList() {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM reviews";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                reviews.add(toReview(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar las reseñas", e);
        }
        return reviews;
    }

    @Override
    public Iterable<Review> findAll() {
        return findAllToList();
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        String sql = "DELETE FROM reviews WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la reseña por ID", e);
        }
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM reviews";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar todas las reseñas", e);
        }
    }

    @Override
    public boolean existsById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        String sql = "SELECT 1 FROM reviews WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al comprobar si existe la reseña", e);
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM reviews";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar las reseñas", e);
        }
        return 0;
    }

    //Métodos propios

    @Override
    public List<Review> findByVideogameId(Long videogameId) {
        if (videogameId == null) {
            throw new IllegalArgumentException("El ID del videojuego no puede ser nulo");
        }
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM reviews WHERE videogame_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, videogameId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reviews.add(toReview(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar reseñas por ID de videojuego", e);
        }
        return reviews;
    }

    @Override
    public double getRecommendationPercentageByVideogameId(Long videogameId) {
        if (videogameId == null) {
            throw new IllegalArgumentException("El ID del videojuego no puede ser nulo");
        }
        String sql = """
            SELECT
                COUNT(*) AS total,
                SUM(CASE WHEN recommended = 1 THEN 1 ELSE 0 END) AS positive
            FROM reviews
            WHERE videogame_id = ?
            """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, videogameId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    long total = rs.getLong("total");
                    if (total == 0) {
                        return 0.0;
                    }
                    long positive = rs.getLong("positive");
                    return (positive * 100.0) / total;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al calcular el porcentaje de recomendación del videojuego", e);
        }
        return 0.0;
    }


    private Review toReview(ResultSet rs) throws SQLException {
        return new Review(
                rs.getLong("id"),
                rs.getLong("account_id"),
                rs.getLong("videogame_id"),
                rs.getBoolean("recommended"),
                rs.getString("comment"),
                LocalDate.parse(rs.getString("publication_date"))
        );
    }
}