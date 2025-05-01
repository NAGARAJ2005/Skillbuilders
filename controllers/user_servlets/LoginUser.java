package com.skillbuilders.user_servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.google.gson.JsonObject;
import com.skillbuilders.dao.UserAuthenticationDAO;

@WebServlet("/loginuser")
public class LoginUser extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LoginUser() {
        super();
    }

    /**
     * Handles POST requests for user login.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set response type to JSON
        response.setContentType("application/json");

        // Create a JSON object to hold the response
        JsonObject jsonResponse = new JsonObject();

        try {
            // Retrieve parameters from the request
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            // Use DAO to check login credentials and fetch user ID
            UserAuthenticationDAO dao = new UserAuthenticationDAO();
            int userId = dao.loginUser(email.trim(), password.trim()); // Get the userId if authenticated

            // Generate appropriate response based on authentication result
            if (userId > 0) {  // User authenticated successfully
                // Store user ID in session
                HttpSession session = request.getSession();
                session.setAttribute("userid", userId);

                // Set session timeout (optional)
                session.setMaxInactiveInterval(30 * 60); // 30 minutes

                // Send a successful login message
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
