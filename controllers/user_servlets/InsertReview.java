package com.skillbuilders.user_servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.skillbuilders.dao.ReviewDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/insertreview")
public class InsertReview extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        try {
            // Parse the incoming JSON from the request body
            BufferedReader reader = request.getReader();
            StringBuilder jsonInput = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonInput.append(line);
            }

            // Convert the JSON string into a JsonObject
            JsonObject jsonObject = gson.fromJson(jsonInput.toString(), JsonObject.class);

            // Extract userId, courseId, reviewText, and rating from the request JSON
            int userId = jsonObject.get("userid").getAsInt();
            int courseId = jsonObject.get("courseid").getAsInt();
            String reviewText = jsonObject.get("review").getAsString();
            int rating = jsonObject.get("rating").getAsInt();

            // Create an instance of InsertReviewDAO to interact with the database
            ReviewDAO dao = new ReviewDAO();
            boolean success = dao.addReview(userId, courseId, rating, reviewText);

            // Prepare the response based on success or failure
            String responseMessage;
            if (success) {
                responseMessage = "{\"status\": \"success\", \"message\": \"Review inserted successfully!\"}";
            } else {
                responseMessage = "{\"status\": \"error\", \"message\": \"Failed to insert review.\"}";
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            response.getWriter().write(responseMessage);

        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"An error occurred while inserting the review.\"}");
        }
    }
}
