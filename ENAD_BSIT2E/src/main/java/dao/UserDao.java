package dao;

import model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/** All SQL for the users table. Every query uses PreparedStatement. */
public class UserDao {

    /** Saves a new account. Status is always PENDING. Returns false if the username is taken. */
    public boolean register(String username, String password, String role) {
        if (usernameExists(username)) {
            return false;
        }
        String sql = "INSERT INTO users (username, password, roles, status) VALUES (?, ?, ?, 'PENDING')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hash(password));
            ps.setString(3, role);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Register failed: " + e.getMessage(), e);
        }
    }

    /** Returns the user if the username and password match a row, otherwise empty. */
    public Optional<User> login(String username, String password) {
        String sql = "SELECT id, username, password, roles, status FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hash(password));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("roles"),
                            rs.getString("status")));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Login failed: " + e.getMessage(), e);
        }
    }

    public boolean usernameExists(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Username check failed: " + e.getMessage(), e);
        }
    }

    /** Creates the default admin (ACTIVE) when the table is empty. */
    public void seedAdmin() {
        String count = "SELECT COUNT(*) FROM users";
        String insert = "INSERT INTO users (username, password, roles, status) VALUES (?, ?, 'ADMIN', 'ACTIVE')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement c = conn.prepareStatement(count);
             ResultSet rs = c.executeQuery()) {
            if (rs.next() && rs.getInt(1) == 0) {
                try (PreparedStatement ps = conn.prepareStatement(insert)) {
                    ps.setString(1, "admin");
                    ps.setString(2, hash("admin123"));
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Seeding admin failed: " + e.getMessage(), e);
        }
    }

    /** SHA-256 so plain passwords are never stored in the database. */
    private String hash(String password) {
        try {
            byte[] bytes = MessageDigest.getInstance("SHA-256").digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
