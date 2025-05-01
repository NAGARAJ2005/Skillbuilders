package com.skillbuilders.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.skillbuilders.util.DBConnection;

public class UpdateTestResultDAO {

    public boolean updateTestResult(int courseId, int userId, int moduleNumber, int totalMarks, int userMarks, String result) {
        String selectQuery = "SELECT user_marks FROM testresult WHERE courseid = ? AND userid = ? AND module_number = ?";
        String insertOrUpdateQuery = "REPLACE INTO testresult (courseid, userid, module_number, total_marks, user_marks, result) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertOrUpdateQuery)) {

            // Check if a record exists with the same courseid, userid, and module_number
            selectStmt.setInt(1, courseId);
            selectStmt.setInt(2, userId);
            selectStmt.setInt(3, moduleNumber);

            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                int existingMarks = rs.getInt("user_marks");
                // If existing marks are greater or equal, skip insertion
                if (existingMarks >= userMarks) {
                    return false;
                }
            }

            // Insert or update the record
            insertStmt.setInt(1, courseId);
            insertStmt.setInt(2, userId);
            insertStmt.setInt(3, moduleNumber);
            insertStmt.setInt(4, totalMarks);
            insertStmt.setInt(5, userMarks);
            insertStmt.setString(6, result);
            insertStmt.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

