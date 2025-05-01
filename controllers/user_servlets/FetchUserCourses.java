package com.skillbuilders.user_servlets;

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

import com.google.gson.Gson;
import com.skillbuilders.dao.FetchCourseDAO;
import com.skillbuilders.util.Course;
import com.skillbuilders.util.DBConnection;


@WebServlet("/fetchusercourses")
public class FetchUserCourses extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final Gson gson = new Gson();
    private int USER_ID = 0;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        
        System.out.println(request.toString());
        System.out.println(request);
        String courseType = request.getParameter("type");
        USER_ID = Integer.parseInt(request.getParameter("userid"));
        System.out.println(request);

        // Call the method to fetch the course IDs based on course type
        List<Integer> courseIds = null;
        if(courseType.equals("cart")) {
        	try { courseIds = cartCourseIds(courseType); } catch (ClassNotFoundException | SQLException e) {	e.printStackTrace(); }
        }
        else if(courseType.equals("favourite")) {
        	try { courseIds = favouriteCourseIds(courseType); } catch (ClassNotFoundException | SQLException e) {	e.printStackTrace(); }
        }
        else if(courseType.equals("enrolled")) {
        	try { courseIds = enrolledCourseIds(courseType); } catch (ClassNotFoundException | SQLException e) {	e.printStackTrace(); }
        }
        else {
        	try { courseIds = completedCourseIds(courseType); } catch (ClassNotFoundException | SQLException e) {	e.printStackTrace(); }

        }
    	//try { courseIds = fetchCourses(courseType); } catch (ClassNotFoundException | SQLException e) {	e.printStackTrace(); }
    	
        Connection connection = null;
		try { connection = DBConnection.getConnection(); } catch (ClassNotFoundException | SQLException e) { e.printStackTrace(); }
		
		System.out.println("Course type : " + courseType);
		System.out.println(courseIds);
		
		try {
		    List<Course> courses = new ArrayList<>();
		    FetchCourseDAO dao = new FetchCourseDAO();

		    // Populate the courses list
		    for (int courseId : courseIds) {
		    	Course course = dao.getCourseDetails(courseId, connection);
		    	System.out.println(course);
		        courses.add(course);
		    }

		    // Convert the courses list to JSON
		    String jsonResponse = gson.toJson(courses);

		    // Set response type and send JSON array
		    response.setContentType("application/json");
		    response.getWriter().write(jsonResponse);

		} catch (Exception e) {
		    e.printStackTrace();
		    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		    response.setContentType("application/json");
		    response.getWriter().write("{\"status\":\"error\",\"message\":\"Failed to fetch course data\"}");
		}
    }
    
 // Method to fetch courses from the database
    public List<Integer> cartCourseIds(String type) throws SQLException, ClassNotFoundException {
        // ArrayList to store course IDs
        List<Integer> courseIds = new ArrayList<>();
        
        // SQL query to get course IDs for the static user ID and the specified course type
        String sql = "SELECT courseid FROM usercourses WHERE userid = ? AND course_type = ?";

        // Ensure connection is closed properly with try-with-resources
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Set parameters to avoid SQL injection
            stmt.setInt(1, USER_ID);
            stmt.setString(2, type);

            // Execute the query and get results
            try (ResultSet rs = stmt.executeQuery()) {
                // Iterate through the result set and add course IDs to the list
                while (rs.next()) {
                    int courseId = rs.getInt("courseid");
                    courseIds.add(courseId);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            // Log the exception for debugging (You can use a logging framework here)
            System.err.println("Error fetching courses: " + e.getMessage());
            throw e; // Rethrow the exception or handle it appropriately
        }

        // Log course IDs to the console for verification
        System.out.println("Retrieved Course IDs: " + courseIds);

        // Return the list of course IDs
        return courseIds;
    }
    
    
    public List<Integer> favouriteCourseIds(String type) throws SQLException, ClassNotFoundException {
        // ArrayList to store course IDs
        List<Integer> courseIds = new ArrayList<>();
        
        // SQL query to get course IDs for the static user ID and the specified course type
        String sql = "SELECT courseid FROM usercourses WHERE userid = ? AND course_type = ?";

        // Ensure connection is closed properly with try-with-resources
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Set parameters to avoid SQL injection
            stmt.setInt(1, USER_ID);
            stmt.setString(2, type);

            // Execute the query and get results
            try (ResultSet rs = stmt.executeQuery()) {
                // Iterate through the result set and add course IDs to the list
                while (rs.next()) {
                    int courseId = rs.getInt("courseid");
                    courseIds.add(courseId);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            // Log the exception for debugging (You can use a logging framework here)
            System.err.println("Error fetching courses: " + e.getMessage());
            throw e; // Rethrow the exception or handle it appropriately
        }

        // Log course IDs to the console for verification
        System.out.println("Retrieved Course IDs: " + courseIds);

        // Return the list of course IDs
        return courseIds;
    }
    
    
    public List<Integer> enrolledCourseIds(String type) throws SQLException, ClassNotFoundException {
        // ArrayList to store course IDs
        List<Integer> courseIds = new ArrayList<>();
        
        // SQL query to get course IDs for the static user ID and the specified course type
        String sql = "SELECT courseid FROM usercourses WHERE userid = ? AND course_type = ? AND progress<100";

        // Ensure connection is closed properly with try-with-resources
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Set parameters to avoid SQL injection
            stmt.setInt(1, USER_ID);
            stmt.setString(2, type);

            // Execute the query and get results
            try (ResultSet rs = stmt.executeQuery()) {
                // Iterate through the result set and add course IDs to the list
                while (rs.next()) {
                    int courseId = rs.getInt("courseid");
                    courseIds.add(courseId);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            // Log the exception for debugging (You can use a logging framework here)
            System.err.println("Error fetching courses: " + e.getMessage());
            throw e; // Rethrow the exception or handle it appropriately
        }

        // Log course IDs to the console for verification
        System.out.println("Retrieved Course IDs: " + courseIds);

        // Return the list of course IDs
        return courseIds;
    }
    
    
    public List<Integer> completedCourseIds(String type) throws SQLException, ClassNotFoundException {
        // ArrayList to store course IDs
        List<Integer> courseIds = new ArrayList<>();
        
        // SQL query to get course IDs for the static user ID and the specified course type
        String sql = "SELECT courseid FROM usercourses WHERE userid = ? AND course_type = 'enrolled' AND progress=100";

        // Ensure connection is closed properly with try-with-resources
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Set parameters to avoid SQL injection
            stmt.setInt(1, USER_ID);
            //stmt.setString(2, type);

            // Execute the query and get results
            try (ResultSet rs = stmt.executeQuery()) {
                // Iterate through the result set and add course IDs to the list
                while (rs.next()) {
                    int courseId = rs.getInt("courseid");
                    courseIds.add(courseId);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            // Log the exception for debugging (You can use a logging framework here)
            System.err.println("Error fetching courses: " + e.getMessage());
            throw e; // Rethrow the exception or handle it appropriately
        }

        // Log course IDs to the console for verification
        System.out.println("Retrieved Course IDs: " + courseIds);

        // Return the list of course IDs
        return courseIds;
    }
}

