	package com.skillbuilders.instructor_servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.google.gson.JsonObject;
import com.skillbuilders.dao.InstructorAuthenticationDAO;

@WebServlet("/logininstructor")
public class LoginInstructor extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LoginInstructor() {
        super();
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        JsonObject jsonResponse = new JsonObject();

        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            InstructorAuthenticationDAO dao = new InstructorAuthenticationDAO();
            int instructorId = dao.loginInstructor(email.trim(), password.trim()); // Get the userId if authenticated

            // Generate appropriate response based on authentication result
            if (instructorId > 0) {  // User authenticated successfully
                // Store user ID in session
                HttpSession session = request.getSession();
                session.setAttribute("instructorid", instructorId);

                System.out.println("Login successful...");
                jsonResponse.addProperty("result", "success");
                jsonResponse.addProperty("message", "Login successful.");
            } else {  // Invalid credentials
                jsonResponse.addProperty("result", "failure");
                jsonResponse.addProperty("message", "Email or password is incorrect.");
            }

        } catch (Exception e) {
            // Handle exceptions and return an error response
            e.printStackTrace();
            jsonResponse.addProperty("result", "failure");
            jsonResponse.addProperty("message", "An error occurred while processing the request.");
        }

        // Write the JSON response
        response.getWriter().write(jsonResponse.toString());
    }
}
