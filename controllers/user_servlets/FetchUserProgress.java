package com.skillbuilders.user_servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.skillbuilders.util.DBConnection;

@WebServlet("/fetchuserprogress")
public class FetchUserProgress extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Connection connection = null;
        try {
            connection = DBConnection.getConnection(); // Get DB connection
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Failed to connect to database.\"}");
            return;
        }

        int userId;
        int courseId;
        try {
            userId = Integer.parseInt(request.getParameter("userid")); // Get user ID from the request
            courseId = Integer.parseInt(request.getParameter("courseid")); // Get course ID from the request
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Invalid user ID or course ID format.\"}");
            return;
        }

        Integer progress = null;
        try {
            progress = getUserProgress(userId, courseId, connection); // Fetch progress
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Failed to fetch progress.\"}");
            return;
        }

        if (progress == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\":\"No progress data found for the given user and course.\"}");
            return;
        }

        // Prepare JSON response
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("progress", progress);

        // Set response type and send JSON
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse.toString());
    }
    
    public Integer getUserProgress(int userId, int courseId, Connection connection) throws SQLException {
        String query = "SELECT progress FROM usercourses WHERE userid = ? AND courseid = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, courseId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("progress"); // Return progress if found
                }
            }
        }
        return null; // Return null if no matching record found
    }
}
