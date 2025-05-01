package com.skillbuilders.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.skillbuilders.util.DBConnection;

public class UserAuthenticationDAO {

    public boolean registerUser(String name, String email, String password) throws SQLException, ClassNotFoundException {
        // Query to check if the email already exists
        String checkEmailQuery = "SELECT COUNT(*) FROM users WHERE email = ?";
        String insertUserQuery = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement checkEmailStmt = connection.prepareStatement(checkEmailQuery)) {
            
            // Check if email already exists
            checkEmailStmt.setString(1, email);
            ResultSet rs = checkEmailStmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                // Email already exists
                return false;
            }
            
            // If email does not exist, insert user details
            try (PreparedStatement insertUserStmt = connection.prepareStatement(insertUserQuery)) {
                insertUserStmt.setString(1, name);
                insertUserStmt.setString(2, email);
                insertUserStmt.setString(3, password);
                
                int rowsInserted = insertUserStmt.executeUpdate();
                return rowsInserted > 0;
            }
        }
    }
    
    public int loginUser(String email, String password) throws ClassNotFoundException, SQLException {
        String checkUserQuery = "SELECT userid FROM users WHERE email = ? AND password = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement checkUserStmt = connection.prepareStatement(checkUserQuery)) {
            checkUserStmt.setString(1, email);
            checkUserStmt.setString(2, password);

            try (ResultSet rs = checkUserStmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("userid");  // Return the user ID if found
                }
            }
        }
        return 0;  // Return 0 if user not found
    }

}
