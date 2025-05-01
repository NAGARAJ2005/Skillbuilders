package com.skillbuilders.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.skillbuilders.util.DBConnection;

public class UpdateUserInformationDAO {

    public boolean updateUsername(String userId, String newValue) throws ClassNotFoundException {
    	System.out.println("Inside dao...");
        return updateField("name", userId, newValue);
    }

    public boolean updateGender(String userId, String newValue) throws ClassNotFoundException {
        return updateField("gender", userId, newValue);
    }

    public boolean updateSummary(String userId, String newValue) throws ClassNotFoundException {
        return updateField("professional_summary", userId, newValue);
    }

    public boolean updateDOB(String userId, String newValue) throws ClassNotFoundException {
        return updateField("DOB", userId, newValue);
    }

    public boolean updatePhoneNumber(String userId, String newValue) throws ClassNotFoundException {
        return updateField("phone_number", userId, newValue);
    }

    public boolean updateCountry(String userId, String newValue) throws ClassNotFoundException {
        return updateField("country", userId, newValue);
    }

    public boolean updateCity(String userId, String newValue) throws ClassNotFoundException {
        return updateField("city", userId, newValue);
    }

    public boolean updateGrade(String userId, String newValue) throws ClassNotFoundException {
        return updateField("grade", userId, newValue);
    }

    public boolean updateCurrentStream(String userId, String newValue) throws ClassNotFoundException {
        return updateField("stream", userId, newValue);
    }
    
    public boolean updateCurrentProfile(String userId, String newValue) throws ClassNotFoundException {
        return updateField("profile", userId, newValue);
    }

    public boolean updateInterestedStreams(String userId, String[] interestedStreams) throws ClassNotFoundException {
        String deleteQuery = "DELETE FROM userinterestedstream WHERE userid = ?";
        String insertQuery = "INSERT INTO userinterestedstream (userid, stream) VALUES (?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery);
             PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {

            // Start transaction
            connection.setAutoCommit(false);

            // Delete existing records
            deleteStmt.setString(1, userId);
            deleteStmt.executeUpdate();

            //if(interestedStreams!=null) {
	            // Insert new records
	            for (String stream : interestedStreams) {
	                insertStmt.setString(1, userId);
	                insertStmt.setString(2, stream);
	                insertStmt.addBatch();
	                System.out.println(insertQuery + " " + stream); 
	            }
	                   
	            insertStmt.executeBatch();
            //}

            // Commit transaction
            connection.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean updateField(String fieldName, String userId, String newValue) throws ClassNotFoundException {
        String query = "UPDATE users SET " + fieldName + " = ? WHERE userid = ?";
        
        int userid = Integer.parseInt(userId);
        System.out.println("Query : " + query + " " + userId + " " + fieldName + " " + newValue);
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newValue);
            preparedStatement.setInt(2, userid);
            System.out.println("Inside here : " + preparedStatement);
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Inside here " + rowsAffected);
            return rowsAffected > 0;

        } catch (SQLException e) {
        	//System.out.println("Inside here error..");
            e.printStackTrace();
            return false;
        }
    }
}

