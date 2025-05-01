// Servlet Class
package com.skillbuilders.servlets.course_servlets;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.skillbuilders.dao.FetchReviewsDAO;

@WebServlet("/getcoursereviews")
public class FetchReviews extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String courseIdParam = request.getParameter("courseid");
        if (courseIdParam == null || courseIdParam.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("error", "courseid parameter is required");
            response.getWriter().write(errorResponse.toString());
            return;
        }

        int courseId;
        try {
            courseId = Integer.parseInt(courseIdParam);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("error", "Invalid courseid format");
            response.getWriter().write(errorResponse.toString());
            return;
        }

        FetchReviewsDAO reviewDAO = new FetchReviewsDAO();
        try {
            List<JsonObject> reviews = reviewDAO.getReviewsByCourseId(courseId);

            JsonArray jsonArray = new JsonArray();
            for (JsonObject review : reviews) {
                jsonArray.add(review);
            }

            response.getWriter().write(jsonArray.toString());
        } catch (ClassNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("error", "Database connection error");
            response.getWriter().write(errorResponse.toString());
        }
    }
}
