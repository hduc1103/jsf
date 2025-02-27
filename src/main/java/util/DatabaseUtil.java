package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import exception.DatabaseException;

public class DatabaseUtil {
    private static final String URL = "jdbc:postgresql://localhost:5432/employee";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123456789";

    public static Connection getConnection() throws SQLException {
    	try {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    	} catch(SQLException e) {
    		 throw new DatabaseException("Cannot connect to database: " + e.getMessage());
    	}
    }
}
