package com.example.schoolmanagement.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Using a file-based SQLite database
    private static final String DATABASE_URL = "jdbc:sqlite:school_management.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }
}
