package com.skillbuilders.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/skillbuilders";
    private static final String JDBC_USERNAME = "skillbuilder";
    private static final String JDBC_PASSWORD = "1234";

    // Private constructor to prevent instantiation
    private DBConnection() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        // Load the JDBC driver (optional for newer versions)
        Class.forName("com.mysql.cj.jdbc.Driver");
        // Return the connection
        return DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
    }
}

