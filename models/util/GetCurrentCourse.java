package com.skillbuilders.util;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;

@WebServlet("/getcurrentcourse")
public class GetCurrentCourse extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public GetCurrentCourse() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // Do not create a new session if it doesn't exist
        JsonObject jsonResponse = new JsonObject();

        System.out.println("GetCurrentCourse servlet called...");

        if (session != null) {
            String courseIdStr = session.getAttribute("currentCourseId").toString(); // Retrieve as String
            if (courseIdStr != null) {
                try {
                    Integer currentCourseId = Integer.parseInt(courseIdStr); // Safely parse to Integer
                    jsonResponse.addProperty("currentCourseId", currentCourseId); // Send courseId in the response
                } catch (NumberFormatException e) {
                    jsonResponse.addProperty("error", "Invalid course ID format in session.");
                }
            } else {
                jsonResponse.addProperty("error", "No course ID found in session.");
            }
        } else {
            jsonResponse.addProperty("error", "Session expired or not found.");
        }


        response.setContentType("application/json");
        response.getWriter().write(jsonResponse.toString());
    }
}