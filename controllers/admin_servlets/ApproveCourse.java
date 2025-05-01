package com.skillbuilders.admin_servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.skillbuilders.dao.AddCourseDAO;

@WebServlet("/approvecourse")
public class ApproveCourse extends HttpServlet {
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

        int courseId = jsonRequest.get("courseId").getAsInt();

        // Call the DAO to approve the course
        AddCourseDAO courseDAO = new AddCourseDAO();
        boolean isApproved = false;
		try {
			isApproved = courseDAO.approveCourse(courseId);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if (isApproved) {
            jsonResponse.addProperty("result", "success");
            jsonResponse.addProperty("message", "Course approved successfully.");
        } else {
            jsonResponse.addProperty("result", "error");
            jsonResponse.addProperty("message", "Failed to approve the course.");
        }

        // Send the JSON response
        response.getWriter().write(jsonResponse.toString());
    }
}
