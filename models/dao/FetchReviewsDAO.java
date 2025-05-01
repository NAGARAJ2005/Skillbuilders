package com.skillbuilders.dao;

import java.sql.*;
import java.util.*;
import com.google.gson.JsonObject;
import com.skillbuilders.util.DBConnection;

public class FetchReviewsDAO {

    public List<JsonObject> getReviewsByCourseId(int courseId) throws ClassNotFoundException {
    	String query = "SELECT r.rating, r.review, u.name " +
                "FROM reviews r " +
                "JOIN users u ON r.userid = u.userid " +
                "WHERE r.courseid = ? " +
                "ORDER BY r.rating DESC, r.date_time DESC " +
                "LIMIT 5";



        List<JsonObject> reviews = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, courseId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    JsonObject review = new JsonObject();
                    review.addProperty("userName", resultSet.getString("name"));
                    review.addProperty("rating", resultSet.getInt("rating"));
                    review.addProperty("reviewContent", resultSet.getString("review"));
                    reviews.add(review);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reviews;
    }
}
