package com.skillbuilders.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.skillbuilders.util.DBConnection;

public class AdminAuthenticationDAO {

    /**
     * Authenticates the admin with the given email and password.
     * 
     * @param email The email of the admin.
     * @param password The password of the admin.
     * @return Returns 1 if the credentials are correct, 0 if they are incorrect.
     * @throws ClassNotFoundException, SQLException
     */
    public int loginAdmin(String email, String password) throws ClassNotFoundException, SQLException {
        String checkAdminQuery = "SELECT email FROM admin WHERE email = ? AND password = ?";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement checkAdminStmt = connection.prepareStatement(checkAdminQuery)) {
            checkAdminStmt.setString(1, email);
            checkAdminStmt.setString(2, password);
            
            try (ResultSet rs = checkAdminStmt.executeQuery()) {
                if (rs.next()) {
                    return 1; // Admin authenticated successfully
                }
            }
        }
        return 0; // Admin not found or incorrect credentials
    }
}
