package com.skillbuilders.dao;

import com.skillbuilders.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RemoveUserCourseDAO {

    // Method to remove a course from the favourites table
    public boolean removeFromFavourites(int userId, int courseId) throws ClassNotFoundException {
        // SQL query to delete the course from the favourites table
    	String sql = "DELETE FROM usercourses WHERE userid = ? AND courseid = ? AND course_type = 'favourite'";
        
        // Using try-with-resources to ensure the connection is closed automatically
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the parameters for userId and courseId
            statement.setInt(1, userId);
            statement.setInt(2, courseId);

            // Execute the update and return whether the row was deleted successfully
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception
            return false;
        }
    }

    // Method to remove a course from the cart table
    public boolean removeFromCart(int userId, int courseId) throws ClassNotFoundException {
        // SQL query to delete the course from the cart table
        String sql = "DELETE FROM usercourses WHERE userid = ? AND courseid = ? AND course_type = 'cart'";
        
        // Using try-with-resources to ensure the connection is closed automatically
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the parameters for userId and courseId
            statement.setInt(1, userId);
            statement.setInt(2, courseId);

            // Execute the update and return whether the row was deleted successfully
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception
            return false;
        }
    }
}
