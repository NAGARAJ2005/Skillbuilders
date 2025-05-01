package com.skillbuilders.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.skillbuilders.util.DBConnection;

public class ReviewDAO {
    private static final String INSERT_REVIEW_SQL = "INSERT INTO reviews (userid, courseid, rating, review) VALUES (?, ?, ?, ?)";

    public boolean addReview(int userid, int courseid, int rating, String review) throws SQLException {
        // Get the database connection from the DBConnection class
        Connection connection = null;
        try {
            connection = DBConnection.getConnection(); // Use the package's connection setup
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_REVIEW_SQL)) {

                // Set parameters
                preparedStatement.setInt(1, userid);
                preparedStatement.setInt(2, courseid);
                preparedStatement.setInt(3, rating);
                preparedStatement.setString(4, review);

                // Execute the update
                preparedStatement.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            throw new SQLException("Error inserting review into the database", e);
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // Ensure connection is closed properly if not managed by DBConnection
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return false;
    }
}


