package com.skillbuilders.servlets.course_servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class UpdateCourseId
 */
@WebServlet("/updatecourseid")

public class UpdateCourseId extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set the response content type
        response.setContentType("application/json");

        // Get the courseId from the request body (assuming it comes in a JSON object)
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        
        // Parse the incoming JSON (assuming it's in the form { "courseId": "someValue" })
        String jsonString = jsonBuilder.toString();
        String courseId = parseCourseId(jsonString);

        // Get the current session
        HttpSession session = request.getSession(true);

        // Update the courseId in the session
        session.setAttribute("currentCourseId", courseId);

        // Send a JSON response back to the frontend confirming the update
        PrintWriter out = response.getWriter();
        out.write("{\"status\":\"success\", \"courseId\":\"" + courseId + "\"}");
        out.flush();
    }

    private String parseCourseId(String jsonString) {
        // Assuming the JSON string is in the format {"courseId": "value"}
        // A real application would use a proper JSON library such as Jackson or Gson
        String courseId = jsonString.substring(jsonString.indexOf(":") + 2, jsonString.lastIndexOf("\""));
        return courseId;
    }
}
