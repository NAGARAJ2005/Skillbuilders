package com.skillbuilders.util;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;

@WebServlet("/getcurrentuser")
public class GetCurrentUser extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public GetCurrentUser() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // Don't create a new session if it doesn't exist
        JsonObject jsonResponse = new JsonObject();
        
        System.out.println("Session servlet called...");
        if (session != null) {
            Integer userId = (Integer) session.getAttribute("userid");
            if (userId != null) {
                jsonResponse.addProperty("userId", userId);  // Send userId in response
            } else {
                jsonResponse.addProperty("error", "No user found in session.");
            }
        } else {
            jsonResponse.addProperty("error", "Session expired.");
        }

        response.setContentType("application/json");
        response.getWriter().write(jsonResponse.toString());
    }
}
