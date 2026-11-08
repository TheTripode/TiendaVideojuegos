package es.dam.di.repository.jdbc;

import es.dam.di.infrastructure.DatabaseConnection;
import es.dam.di.model.Library;
import es.dam.di.repository.ILibraryRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LibraryRepositoryJdbc implements ILibraryRepository {

    @Override
    public <S extends Library> S save(S entity) {
        if (entity == null) {
            throw new IllegalArgumentException("La entidad a guardar no puede ser nula");
        }

        //Reformateo de código para que quede más corto
        if (findByAccountIdAndVideogameId(entity.getAccountId(), entity.getVideogameId()).isPresent()) {
            // UPDATE
            String sql = "UPDATE libraries SET purchase_date = ?, hours_played = ? WHERE account_id = ? AND videogame_id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, entity.getPurchaseDate().toString());
                stmt.setBigDecimal(2, entity.getHoursPlayed() != null ? entity.getHoursPlayed() : BigDecimal.ZERO);
                stmt.setLong(3, entity.getAccountId());
                stmt.setLong(4, entity.getVideogameId());

                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Error al actualizar el registro de la biblioteca", e);
            }
        } else {
            // INSERT
            String sql = "INSERT INTO libraries (account_id, videogame_id, purchase_date, hours_played) VALUES (?, ?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setLong(1, entity.getAccountId());
                stmt.setLong(2, entity.getVideogameId());
                stmt.setString(3, entity.getPurchaseDate().toString());
                stmt.setBigDecimal(4, entity.getHoursPlayed() != null ? entity.getHoursPlayed() : BigDecimal.ZERO);

                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Error al insertar el registro en la biblioteca", e);
            }
        }
        return entity;
    }

    @Override
    public Library findById(Long id) {
        throw new UnsupportedOperationException("No se puede, tiene clave compuesta");
    }

    @Override
    public Optional<Library> findByIdOptional(Long id) {
        return Optional.ofNullable(findById(id));
    }

    @Override
    public List<Library> findAllToList() {
        List<Library> libraries = new ArrayList<>();
        String sql = "SELECT * FROM libraries";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                libraries.add(toLibrary(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar los registros de la biblioteca", e);
        }
        return libraries;
    }

    @Override
    public Iterable<Library> findAll() {
        return findAllToList();
    }

    @Override
    public void deleteById(Long id) {
        throw new UnsupportedOperationException("No se puede, tiene clave compuesta");
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM libraries";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar todos los registros de la biblioteca", e);
        }
    }

    @Override
    public boolean existsById(Long id) {
        throw new UnsupportedOperationException("No se puede, tiene clave compuesta");
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM libraries";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar los registros de la biblioteca", e);
        }
        return 0;
    }

    //Métodos propios

    @Override
    public List<Library> findByAccountId(Long accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("El ID de la cuenta no puede ser nulo");
        }
        List<Library> libraries = new ArrayList<>();
        String sql = "SELECT * FROM libraries WHERE account_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    libraries.add(toLibrary(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar la biblioteca por ID de cuenta", e);
        }
        return libraries;
    }

    @Override
    public Optional<Library> findByAccountIdAndVideogameId(Long accountId, Long videogameId) {
        if (accountId == null || videogameId == null) {
            throw new IllegalArgumentException("Los IDs no pueden ser nulos");
        }
        String sql = "SELECT * FROM libraries WHERE account_id = ? AND videogame_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, accountId);
            stmt.setLong(2, videogameId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(toLibrary(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar en biblioteca por cuenta y videojuego", e);
        }
        return Optional.empty();
    }

    private Library toLibrary(ResultSet rs) throws SQLException {
        return new Library(
                rs.getLong("account_id"),
                rs.getLong("videogame_id"),
                LocalDate.parse(rs.getString("purchase_date")),
                rs.getBigDecimal("hours_played")
        );
    }
}