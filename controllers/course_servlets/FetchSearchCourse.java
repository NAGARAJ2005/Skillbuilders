package com.skillbuilders.servlets.course_servlets;

import com.google.gson.Gson;
import com.skillbuilders.dao.FetchCourseDAO;
import com.skillbuilders.util.Course;
import com.skillbuilders.util.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/coursesearch")
public class FetchSearchCourse extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the search string from the request
        String searchString = request.getParameter("searchString");

        if (searchString == null || searchString.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Missing or empty searchString parameter\"}");
            return;
        }

        // Retrieve the user ID from the session
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"No active session found\"}");
            return;
        }

        String userId = session.getAttribute("userid").toString();
        if (userId == null || userId.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"User ID not found in session\"}");
            return;
        }

        // Initialize the DAO and course list
        FetchCourseDAO fetchCourseDAO = new FetchCourseDAO();
        List<Course> courseList = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection()) {
            // Step 1: Fetch course IDs matching the search string and approved status
            String courseQuery = "SELECT courseid FROM courses WHERE name LIKE ? AND approved = 'true'";
            try (PreparedStatement courseStmt = connection.prepareStatement(courseQuery)) {
                courseStmt.setString(1, "%" + searchString + "%");
                try (ResultSet courseRs = courseStmt.executeQuery()) {

                    // Step 2: Fetch enrolled course IDs for the user
                    List<Integer> enrolledCourseIds = new ArrayList<>();
                    String userCoursesQuery = "SELECT courseid FROM usercourses WHERE userid = ?";
                    try (PreparedStatement userCoursesStmt = connection.prepareStatement(userCoursesQuery)) {
                        userCoursesStmt.setString(1, userId);
                        try (ResultSet userCoursesRs = userCoursesStmt.executeQuery()) {
                            while (userCoursesRs.next()) {
                                enrolledCourseIds.add(userCoursesRs.getInt("courseid"));
                            }
                        }
                    }

                    // Step 3: Loop through and fetch details of non-enrolled courses
                    while (courseRs.next()) {
                        int courseId = courseRs.getInt("courseid");
                        if (!enrolledCourseIds.contains(courseId)) {
                            Course course = fetchCourseDAO.getCourseDetails(courseId, connection);
                            if (course != null) {
                                courseList.add(course);
                            }
                        }
                    }
                }
            }

            // Convert the list of courses to JSON and send the response
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(courseList);
            response.setContentType("application/json");
            response.getWriter().write(jsonResponse);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database driver not found");
        }
    }
}
