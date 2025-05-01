package com.skillbuilders.user_servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.skillbuilders.dao.RemoveUserCourseDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/removefromcart")
public class RemoveFromCart extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Create a reference to the DAO
    private RemoveUserCourseDAO courseDAO = new RemoveUserCourseDAO();

    // Protected method to handle the POST request
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set the response content type as JSON
        response.setContentType("application/json");

        // Read the incoming JSON data from the request body
        StringBuilder requestData = new StringBuilder();
        String line;
        while ((line = request.getReader().readLine()) != null) {
            requestData.append(line);
        }

        // Parse the incoming JSON into a JsonObject
        Gson gson = new Gson();
        JsonObject jsonData = gson.fromJson(requestData.toString(), JsonObject.class);

        // Extract data from the JSON request
        int userId = jsonData.get("userid").getAsInt();
        int courseId = jsonData.get("courseid").getAsInt();

        // Call the DAO method to remove the course from the cart
        boolean success = false;
		try {
			success = courseDAO.removeFromCart(userId, courseId);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // Prepare the response message based on the operation result
        JsonObject jsonResponse = new JsonObject();
        if (success) {
            jsonResponse.addProperty("message", "Course removed from cart successfully!");
        } else {
            jsonResponse.addProperty("message", "Failed to remove course from cart.");
        }

        // Write the JSON response to the client
        PrintWriter out = response.getWriter();
        out.write(jsonResponse.toString());
    }
}
