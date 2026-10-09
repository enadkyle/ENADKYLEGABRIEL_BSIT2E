package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/** Opens the SQLite connection and creates the tables on first run. */
public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:towgo.db";

    private DatabaseConnection() { }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    /** Creates the users table if it does not exist yet. */
    public static void initialize() {
        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    id       INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL,
                    roles    TEXT NOT NULL,
                    status   TEXT NOT NULL DEFAULT 'PENDING'
                )
                """;
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Could not set up the database: " + e.getMessage(), e);
        }
    }
}
