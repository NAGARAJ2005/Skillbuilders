package com.skillbuilders.user_servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.*;
import java.util.*;
import com.google.gson.*;
import com.skillbuilders.dao.EnrolledCourseDAO;


@WebServlet("/addtoenrolled")
public class AddToEnrolled extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set response type
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Get the user ID from the session
        HttpSession session = request.getSession(false);
        Integer userId = (Integer) session.getAttribute("userid");

        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write(new Gson().toJson(Collections.singletonMap("message", "User not logged in")));
            return;
        }

        // Get course IDs from the request
        String[] courseIds = request.getParameter("courseids").split(",");
        System.out.println(courseIds);
        List<Integer> courseIdList = new ArrayList<>();
        for (String courseId : courseIds) {
            courseIdList.add(Integer.parseInt(courseId.trim()));
        }

        // Use DAO to update course types
        EnrolledCourseDAO dao = new EnrolledCourseDAO();
        boolean isUpdated = false;
		try {
			isUpdated = dao.updateCourseTypeToEnrolled(userId, courseIdList);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(isUpdated);

        // Return JSON response
        if (isUpdated) {
            out.write(new Gson().toJson(Collections.singletonMap("message", "Successfully enrolled")));
            return;
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write(new Gson().toJson(Collections.singletonMap("message", "Failed to enroll courses")));
        }
    }
}
