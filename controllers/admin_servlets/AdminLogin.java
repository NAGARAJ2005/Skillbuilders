package com.skillbuilders.admin_servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.JsonObject;
import com.skillbuilders.dao.AdminAuthenticationDAO;
import com.google.gson.JsonParser;

@WebServlet("/loginadmin")
public class AdminLogin extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AdminLogin() {
        super();
    }

    /**
     * Handles POST requests for admin login.
     */
    

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set response type to JSON
        response.setContentType("application/json");

        // Create a JSON object to hold the response
        JsonObject jsonResponse = new JsonObject();

        try {
            // Read the JSON body from the request
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                sb.append(line);
            }

            // Parse the JSON string into a JsonObject
            JsonObject jsonRequest = JsonParser.parseString(sb.toString()).getAsJsonObject();
            
            // Retrieve email and password from the JSON body
            String email = jsonRequest.get("email").getAsString();
            String password = jsonRequest.get("password").getAsString();

            // Check if the email or password is missing
            if (email == null || email.trim().isEmpty()) {
                jsonResponse.addProperty("result", "failure");
                jsonResponse.addProperty("message", "Email is required.");
                response.getWriter().write(jsonResponse.toString());
                return;
            }

            if (password == null || password.trim().isEmpty()) {
                jsonResponse.addProperty("result", "failure");
                jsonResponse.addProperty("message", "Password is required.");
                response.getWriter().write(jsonResponse.toString());
                return;
            }

            // Use DAO to check admin credentials
            AdminAuthenticationDAO dao = new AdminAuthenticationDAO();
            int result = dao.loginAdmin(email.trim(), password.trim());

            if (result == 1) {
                jsonResponse.addProperty("result", "success");
                jsonResponse.addProperty("message", "Login successful.");
            } else {
                jsonResponse.addProperty("result", "failure");
                jsonResponse.addProperty("message", "Invalid credentials.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.addProperty("result", "failure");
            jsonResponse.addProperty("message", "An error occurred during login.");
        }

        // Send the JSON response
        response.getWriter().write(jsonResponse.toString());
    }

}
