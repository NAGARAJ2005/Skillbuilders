package com.skillbuilders.servlets.course_servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.skillbuilders.dao.FetchCourseDAO;
import com.skillbuilders.util.Course;
import com.skillbuilders.util.DBConnection;

/**
 * Servlet implementation class FetchCourseById
 */
@WebServlet("/fetchcoursebyid")
public class FetchCourseById extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Declare Gson object at class level
    private final Gson gson = new Gson();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        FetchCourseDAO dao = new FetchCourseDAO();

        Connection connection = null;
        try {
            connection = DBConnection.getConnection(); // Get DB connection
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Failed to connect to database.\"}");
            return; // Stop further execution
        }

        int courseId;
        try {
            courseId = Integer.parseInt(request.getParameter("courseid")); // Get course ID from the request
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Invalid course ID format.\"}");
            return; // Stop further execution
        }

        // Store course ID in session
        HttpSession session = request.getSession();
        session.setAttribute("currentCourseId", courseId);

        Course course = null;
        try {
            course = dao.getCourseDetails(courseId, connection); // Fetch course details
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Failed to fetch course details.\"}");
            return; // Stop further execution
        }

        if (course == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\":\"Course not found.\"}");
            return; // Stop further execution
        }

        // Convert the course object to JSON
        String jsonResponse = gson.toJson(course);

        // Set response type and send JSON response
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
    }
}
