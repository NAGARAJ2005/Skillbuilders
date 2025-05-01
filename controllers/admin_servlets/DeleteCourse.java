package com.skillbuilders.admin_servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.skillbuilders.dao.AddCourseDAO;

@WebServlet("/deletecourse")
public class DeleteCourse extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("deprecation")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        JsonObject jsonResponse = new JsonObject();

        // Read the JSON request body
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = request.getReader().readLine()) != null) {
            stringBuilder.append(line);
        }
        JsonObject jsonRequest = new JsonObject();
        try {
            jsonRequest = new com.google.gson.JsonParser().parse(stringBuilder.toString()).getAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Integer courseId = jsonRequest.get("courseId").getAsInt();

        // Call the DAO to delete the course
        AddCourseDAO courseDAO = new AddCourseDAO();
        boolean isDeleted = false;
		try {
			isDeleted = courseDAO.deleteCourse(courseId.toString());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if (isDeleted) {
            jsonResponse.addProperty("result", "success");
            jsonResponse.addProperty("message", "Course deleted successfully.");
        } else {
            jsonResponse.addProperty("result", "error");
            jsonResponse.addProperty("message", "Failed to delete the course.");
        }

        // Send the JSON response
        response.getWriter().write(jsonResponse.toString());
    }
}
