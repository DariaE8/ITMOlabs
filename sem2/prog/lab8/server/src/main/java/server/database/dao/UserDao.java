package server.database.dao;

import auth_utils.User;

import java.sql.*;
import java.util.Optional;

public class UserDao {
    private final Connection connection;

    public UserDao(Connection connection) {
        this.connection = connection;
    }

    public Integer register(String username, String passwordHash) throws SQLException {
        if (authenticate(username, passwordHash) != null) return null;

        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?) RETURNING id";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, passwordHash);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            return null;
        }
    }

    public Integer authenticate(String username, String passwordHash) throws SQLException {
        String sql = "SELECT id FROM users WHERE username = ? AND password_hash = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, passwordHash);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            return null;
        }
    }

    public Optional<User> findByUsername(String username) throws SQLException {
        String sql = "SELECT id, password_hash FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String passwordHash = rs.getString("password_hash");
                return Optional.of(new User(id, username, passwordHash));
            } else {
                return Optional.empty();
            }
        }
    }
}
