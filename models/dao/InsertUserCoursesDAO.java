package com.skillbuilders.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.skillbuilders.util.DBConnection;

public class InsertUserCoursesDAO {

    // Method to add a course to the usercourses table (for both favourites and cart)
    public boolean addCourseToUser(int userId, int courseId, String courseType) throws SQLException, ClassNotFoundException {
        String checkCourseQuery = "SELECT COUNT(*) FROM usercourses WHERE userid = ? AND courseid = ? AND course_type = ?";
        String insertCourseQuery = "INSERT INTO usercourses (userid, courseid, course_type, progress) VALUES (?, ?, ?, 0)";
        
        System.out.println(insertCourseQuery + " " + userId + " " + courseId + " " + courseType);
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement checkCourseStmt = connection.prepareStatement(checkCourseQuery)) {

            // Check if the course is already added for the user in the specified course type (favourites or cart)
            checkCourseStmt.setInt(1, userId);
            checkCourseStmt.setInt(2, courseId);
            checkCourseStmt.setString(3, courseType);
            ResultSet rs = checkCourseStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                // Course already exists for the user, no need to insert again
                return false;
            }

            // If course doesn't exist, insert it into the usercourses table
            try (PreparedStatement insertCourseStmt = connection.prepareStatement(insertCourseQuery)) {
                insertCourseStmt.setInt(1, userId);
                insertCourseStmt.setInt(2, courseId);
                insertCourseStmt.setString(3, courseType);
                int rowsInserted = insertCourseStmt.executeUpdate();
                return rowsInserted > 0;
            }
        }
    }
}
