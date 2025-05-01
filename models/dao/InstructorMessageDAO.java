package com.skillbuilders.dao;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.skillbuilders.util.InstructorMessage;
import com.skillbuilders.util.DBConnection;

public class InstructorMessageDAO {

    // Method to insert a new instructor message
    public int addMessage(InstructorMessage message) throws SQLException, ClassNotFoundException {
        String insertQuery = "INSERT INTO instructor_messages (instructorid, courseid, name, message) VALUES (?, ?, ?, ?)";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, message.getInstructorId());
            stmt.setInt(2, message.getCourseId());
            stmt.setString(3, message.getName());
            stmt.setString(4, message.getMessage());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);  // Return the generated message ID
                    }
                }
            }
        }
        return 0;  // Return 0 if insertion failed
    }

    // Method to delete a message by its ID
    public boolean deleteMessage(int messageId) throws SQLException, ClassNotFoundException {
        String deleteQuery = "DELETE FROM instructor_messages WHERE messageid = ?";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(deleteQuery)) {
            
            stmt.setInt(1, messageId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;  // Return true if deletion was successful
        }
    }

    // Method to retrieve a list of messages for a specific instructor
    public List<InstructorMessage> getMessagesByInstructorId(int instructorId) throws SQLException, ClassNotFoundException {
        String selectQuery = "SELECT messageid, instructorid, courseid, name, message, viewed FROM instructor_messages WHERE instructorid = ?";
        List<InstructorMessage> messages = new ArrayList<>();
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(selectQuery)) {
            
            stmt.setInt(1, instructorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    InstructorMessage message = new InstructorMessage(
                        rs.getInt("messageid"),
                        rs.getInt("instructorid"),
                        rs.getInt("courseid"),
                        rs.getString("name"),
                        rs.getString("message"),
                        rs.getString("viewed")  // 'viewed' can be handled later in your application if needed
                    );
                    messages.add(message);
                }
            }
        }
        return messages;  // Return the list of messages
    }
}
