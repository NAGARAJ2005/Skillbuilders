package com.skillbuilders.dao;

import java.sql.*;
import java.util.*;

import com.skillbuilders.util.DBConnection;

public class EnrolledCourseDAO {

    public boolean updateCourseTypeToEnrolled(int userId, List<Integer> courseIds) throws ClassNotFoundException {
        String updateQuery = "UPDATE usercourses SET course_type = 'enrolled' WHERE userid = ? AND courseid = ? AND course_type = 'cart'";
        String transactionQuery = "INSERT INTO transactions (amount, courseid, userid, details) VALUES (?, ?, ?, ?)";
        String fetchCourseDetailsQuery = "SELECT price, name FROM courses WHERE courseid = ?";
        
        try (Connection connection = DBConnection.getConnection()) {
            // Update usercourses
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                for (int courseId : courseIds) {
                    updateStatement.setInt(1, userId);
                    updateStatement.setInt(2, courseId);
                    updateStatement.addBatch();
                }
                System.out.println("Received course IDs: " + courseIds);

                int[] updateCounts = updateStatement.executeBatch();
                for (int count : updateCounts) {
                    if (count == 0) {
                        System.out.println("Update failed for one or more courses.");
                        return false; // If any update fails, return false
                    }
                }
                System.out.println("Update success in usercourses.");
            }

            // Insert into transactions table
            try (PreparedStatement transactionStatement = connection.prepareStatement(transactionQuery);
                 PreparedStatement fetchCourseDetailsStatement = connection.prepareStatement(fetchCourseDetailsQuery)) {

                for (int courseId : courseIds) {
                    // Fetch course price and name
                    fetchCourseDetailsStatement.setInt(1, courseId);
                    try (ResultSet rs = fetchCourseDetailsStatement.executeQuery()) {
                        if (rs.next()) {
                            float price = rs.getFloat("price");
                            String courseName = rs.getString("name");

                            // Insert into transactions table
                            transactionStatement.setFloat(1, price);
                            transactionStatement.setInt(2, courseId);
                            transactionStatement.setInt(3, userId);
                            transactionStatement.setString(4, courseName);
                            transactionStatement.addBatch();
                        } else {
                            System.out.println("Course details not found for course ID: " + courseId);
                            return false; // If course details are not found, return false
                        }
                    }
                }

                int[] transactionCounts = transactionStatement.executeBatch();
                for (int count : transactionCounts) {
                    if (count == 0) {
                        System.out.println("Transaction insert failed for one or more courses.");
                        return false; // If any insert fails, return false
                    }
                }
                System.out.println("Insert success in transactions.");
            }

            return true; // All updates and inserts successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
