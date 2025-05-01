package com.skillbuilders.instructor_servlets;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.skillbuilders.dao.InstructorAuthenticationDAO;
import com.google.gson.JsonObject;

@WebServlet("/registerinstructor")
public class RegisterInstructor extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public RegisterInstructor() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        // Get form data
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        
        System.out.println("Values : " + name + " " + email + " " + password);
        InstructorAuthenticationDAO dao = new InstructorAuthenticationDAO();
       

        JsonObject jsonResponse = new JsonObject();

        try {
            boolean isRegistered = dao.registerInstructor(name, email, password);

            if (isRegistered) {
                // Return success response with message
                jsonResponse.addProperty("result", "success");
                jsonResponse.addProperty("message", "Registration successful.");
            } else {
                // Return failure response with message
                jsonResponse.addProperty("result", "failure");
                jsonResponse.addProperty("message", "Email is already registered. Please try a different email.");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Return failure response with error message
            System.out.println("Error is : " + e.getMessage());
            jsonResponse.addProperty("result", "failure");
            jsonResponse.addProperty("message", "An error occurred during registration. Please try again later.");
        }

        // Send the JSON response to the client
        response.getWriter().write(jsonResponse.toString());
    }
}

