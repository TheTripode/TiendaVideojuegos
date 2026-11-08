package es.dam.di.repository.jdbc;

import es.dam.di.infrastructure.DatabaseConnection;
import es.dam.di.model.Account;
import es.dam.di.model.Email;
import es.dam.di.repository.IAccountRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountRepositoryJdbc implements IAccountRepository {

    @Override
    public <S extends Account> S save(S entity) {
        if (entity == null) {
            throw new IllegalArgumentException("La entidad a guardar no puede ser nula");
        }

        if (entity.getId() == null) {
            // Si no tiene ID es insert
            String sql = "INSERT INTO accounts (user_name, email, password, registration_date) VALUES (?, ?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 // Statement.RETURN_GENERATED_KEYS para guardar el ID que la BD le asigna
                 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, entity.getUserName());
                stmt.setString(2, entity.getEmail().getValue());
                stmt.setString(3, entity.getPassword());
                stmt.setString(4, entity.getRegistrationDate().toString());

                stmt.executeUpdate();

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) { // buscamos el ID
                        entity.setId(generatedKeys.getLong(1)); // extrae el ID
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al insertar la cuenta", e);
            }
        } else {
            // Si tiene ID es update
            String sql = "UPDATE accounts SET user_name = ?, email = ?, password = ?, registration_date = ? WHERE id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, entity.getUserName());
                stmt.setString(2, entity.getEmail().getValue());
                stmt.setString(3, entity.getPassword());
                stmt.setString(4, entity.getRegistrationDate().toString());
                stmt.setLong(5, entity.getId());

                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Error al actualizar la cuenta", e);
            }
        }
        return entity;
    }

    @Override
    public Account findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        String sql = "SELECT * FROM accounts WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return toAccount(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cuenta por ID", e);
        }
        return null;
    }

    @Override
    public Optional<Account> findByIdOptional(Long id) {
        return Optional.ofNullable(findById(id));
    }

    @Override
    public List<Account> findAllToList() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                accounts.add(toAccount(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar las cuentas", e);
        }
        return accounts;
    }

    @Override
    public Iterable<Account> findAll() {
        return findAllToList();
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        String sql = "DELETE FROM accounts WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la cuenta por ID", e);
        }
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM accounts";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar todas las cuentas", e);
        }
    }

    @Override
    public boolean existsById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        String sql = "SELECT 1 FROM accounts WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al comprobar si existe la cuenta", e);
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM accounts";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar las cuentas", e);
        }
        return 0;
    }

    //Métodos propios

    @Override
    public Optional<Account> findByEmail(Email email) {
        if (email == null) {
            throw new IllegalArgumentException("El email no puede ser nulo");
        }
        String sql = "SELECT * FROM accounts WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email.getValue());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(toAccount(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cuenta por email", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Account> findByUserName(String userName) {
        if (userName == null) {
            throw new IllegalArgumentException("El nombre de usuario no puede ser nulo");
        }
        String sql = "SELECT * FROM accounts WHERE user_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(toAccount(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cuenta por nombre de usuario", e);
        }
        return Optional.empty();
    }

    //para acortar la forma de devolver la entidad
    private Account toAccount(ResultSet rs) throws SQLException {
        return new Account(
                rs.getLong("id"),
                rs.getString("user_name"),
                new Email(rs.getString("email")),
                rs.getString("password"),
                LocalDate.parse(rs.getString("registration_date"))
        );
    }
}