package com.skillbuilders.user_servlets;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.skillbuilders.dao.UserAuthenticationDAO;

@WebServlet("/registeruser")
public class RegisterUser extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public RegisterUser() {
        super();
    }

    /**
     * Handles POST requests for user registration.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set the response content type to JSON
        response.setContentType("application/json");
        
        /*
        System.out.println("Received request : " + request);
        request.getParameterMap().forEach((key, value) -> {
            System.out.println(key + ": " + String.join(", ", value));
        });
        */

        // Get form data
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Instantiate DAO
        UserAuthenticationDAO dao = new UserAuthenticationDAO();

        JsonObject jsonResponse = new JsonObject();

        try {
            // Check registration status
            boolean isRegistered = dao.registerUser(name, email, password);

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
            jsonResponse.addProperty("result", "failure");
            jsonResponse.addProperty("message", "An error occurred during registration. Please try again later.");
        }

        // Send the JSON response to the client
        response.getWriter().write(jsonResponse.toString());
    }
}
