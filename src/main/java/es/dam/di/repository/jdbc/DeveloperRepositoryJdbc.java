package es.dam.di.repository.jdbc;

import es.dam.di.infrastructure.DatabaseConnection;
import es.dam.di.model.Developer;
import es.dam.di.repository.IDeveloperRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DeveloperRepositoryJdbc implements IDeveloperRepository {

    @Override
    public <S extends Developer> S save(S entity) {
        if (entity == null) {
            throw new IllegalArgumentException("La entidad a guardar no puede ser nula");
        }

        if (entity.getId() == null) {
            // INSERT
            String sql = "INSERT INTO developers (name) VALUES (?)";
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
                throw new RuntimeException("Error al insertar el desarrollador", e);
            }
        } else {
            // UPDATE
            String sql = "UPDATE developers SET name = ? WHERE id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, entity.getName());
                stmt.setLong(2, entity.getId());

                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Error al actualizar el desarrollador", e);
            }
        }
        return entity;
    }

    @Override
    public Developer findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        String sql = "SELECT * FROM developers WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return toDeveloper(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar desarrollador por ID", e);
        }
        return null;
    }

    @Override
    public Optional<Developer> findByIdOptional(Long id) {
        return Optional.ofNullable(findById(id));
    }

    @Override
    public List<Developer> findAllToList() {
        List<Developer> developers = new ArrayList<>();
        String sql = "SELECT * FROM developers";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                developers.add(toDeveloper(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar los desarrolladores", e);
        }
        return developers;
    }

    @Override
    public Iterable<Developer> findAll() {
        return findAllToList();
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        String sql = "DELETE FROM developers WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el desarrollador por ID", e);
        }
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM developers";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar todos los desarrolladores", e);
        }
    }

    @Override
    public boolean existsById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        String sql = "SELECT 1 FROM developers WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al comprobar si existe el desarrollador", e);
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM developers";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar los desarrolladores", e);
        }
        return 0;
    }

    //Métodos propios

    @Override
    public Optional<Developer> findByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("El nombre no puede ser nulo");
        }
        String sql = "SELECT * FROM developers WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(toDeveloper(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar desarrollador por nombre", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Developer> findByNameContaining(String name) {
        if (name == null) {
            throw new IllegalArgumentException("El nombre no puede ser nulo");
        }

        List<Developer> developers = new ArrayList<>();
        String sql = "SELECT * FROM developers WHERE name LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + name + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    developers.add(toDeveloper(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar desarrolladores con nombre que contenga: " + name, e);
        }

        return developers;
    }

    private Developer toDeveloper(ResultSet rs) throws SQLException {
        return new Developer(
                rs.getLong("id"),
                rs.getString("name")
        );
    }
}