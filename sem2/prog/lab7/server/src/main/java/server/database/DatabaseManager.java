package server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/labs";
    private static final String DB_USER = "drysua";
    private static final String DB_PASSWORD = "cdvfbgnh";

    private static Connection connection;

    public static Connection getConnection() {
        try{
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
            return connection;
        } catch (SQLException e) {
            return null;
        }

    }
}
