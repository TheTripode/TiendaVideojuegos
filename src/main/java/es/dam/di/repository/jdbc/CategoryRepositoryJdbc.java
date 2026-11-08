package es.dam.di.repository.jdbc;

import es.dam.di.infrastructure.DatabaseConnection;
import es.dam.di.model.Category;
import es.dam.di.repository.ICategoryRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryRepositoryJdbc implements ICategoryRepository {

    @Override
    public <S extends Category> S save(S entity) {
        if (entity == null) {
            throw new IllegalArgumentException("La entidad a guardar no puede ser nula");
        }

        if (entity.getId() == null) {
            // INSERT
            String sql = "INSERT INTO categories (name) VALUES (?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, entity.getName());
                stmt.executeUpdate();

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        entity.setId(generatedKeys.getLong(1));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al insertar la categoría", e);
            }
        } else {
            // UPDATE
            String sql = "UPDATE categories SET name = ? WHERE id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, entity.getName());
                stmt.setLong(2, entity.getId());

                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Error al actualizar la categoría", e);
            }
        }
        return entity;
    }

    @Override
    public Category findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        String sql = "SELECT * FROM categories WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return toCategory(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar categoría por ID", e);
        }
        return null;
    }

    @Override
    public Optional<Category> findByIdOptional(Long id) {
        return Optional.ofNullable(findById(id));
    }

    @Override
    public List<Category> findAllToList() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                categories.add(toCategory(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar las categorías", e);
        }
        return categories;
    }

    @Override
    public Iterable<Category> findAll() {
        return findAllToList();
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        String sql = "DELETE FROM categories WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la categoría por ID", e);
        }
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM categories";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar todas las categorías", e);
        }
    }

    @Override
    public boolean existsById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        String sql = "SELECT 1 FROM categories WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al comprobar si existe la categoría", e);
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM categories";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar las categorías", e);
        }
        return 0;
    }

    //Métodos propios

    @Override
    public Optional<Category> findByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("El nombre no puede ser nulo");
        }
        String sql = "SELECT * FROM categories WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(toCategory(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar categoría por nombre", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Category> findByVideogameId(Long videogameId) {
        if (videogameId == null) {
            throw new IllegalArgumentException("El ID del videojuego no puede ser nulo");
        }

        List<Category> categories = new ArrayList<>();
        String sql = "SELECT c.* FROM categories c JOIN videogames_categories vc ON c.id = vc.category_id WHERE vc.videogame_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, videogameId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    categories.add(toCategory(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar categorías del videojuego con ID: " + videogameId, e);
        }

        return categories;
    }

    private Category toCategory(ResultSet rs) throws SQLException {
        return new Category(
                rs.getLong("id"),
                rs.getString("name")
        );
    }
}