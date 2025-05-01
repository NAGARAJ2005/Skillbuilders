package com.skillbuilders.instructor_servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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


@WebServlet("/fetchinstructorcourses")
public class FetchInstructorCourses extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final Gson gson = new Gson();
    private Integer instructorId = null; // Replace with dynamic ID if needed.

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        
        HttpSession session = request.getSession(false); // Use `false` to avoid creating a new session

        if (session != null) {
            instructorId = (Integer) session.getAttribute("instructorid");
        }

        // Check if the session or INSTRUCTOR_ID is missing
        if (session == null || instructorId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Session has ended. Please log in again.\"}");
            return;
        }

        String courseStatus = request.getParameter("status");
        System.out.println("Course status: " + courseStatus);
        List<Course> courses = new ArrayList<>();
        Connection connection = null;

        try {
            connection = DBConnection.getConnection();
            FetchCourseDAO dao = new FetchCourseDAO();

            if ("uploaded".equalsIgnoreCase(courseStatus)) {
                courses = getUploadedCourses(instructorId, dao, connection);
            } else if ("inprogress".equalsIgnoreCase(courseStatus)) {
                courses = getInProgressCourses(instructorId, dao, connection);
            } else {
                throw new IllegalArgumentException("Invalid course status");
            }

            // Convert the courses list to JSON
            String jsonResponse = gson.toJson(courses);

            // Send the response
            response.getWriter().write(jsonResponse);

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Failed to fetch course data\"}");
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    // Function to get uploaded courses (approved = true)
    private List<Course> getUploadedCourses(int instructorId, FetchCourseDAO dao, Connection connection) throws SQLException {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT courseid FROM courses WHERE instructorid = ? AND approved = 'true'";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, instructorId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int courseId = resultSet.getInt("courseid");
                    Course course = dao.getCourseDetails(courseId, connection);
                    if (course != null) {
                        courses.add(course);
                    }
                }
            }
        }
        System.out.println(courses);        
        return courses;
    }

    // Function to get in-progress courses (approved = false)
    private List<Course> getInProgressCourses(int instructorId, FetchCourseDAO dao, Connection connection) throws SQLException {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT courseid FROM courses WHERE instructorid = ? AND approved='false'";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, instructorId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int courseId = resultSet.getInt("courseid");
                    Course course = dao.getCourseDetails(courseId, connection);
                    if (course != null) {
                        courses.add(course);
                    }
                }
            }
        }
        return courses;
    }
}
