package com.skillbuilders.user_servlets;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skillbuilders.dao.UpdateTestResultDAO;

@WebServlet("/updatetestresult")
public class UpdateTestResult extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	        throws ServletException, IOException {
	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");

	    // Read JSON payload from the request body
	    StringBuilder jsonPayload = new StringBuilder();
	    BufferedReader reader = request.getReader();
	    String line;
	    while ((line = reader.readLine()) != null) {
	        jsonPayload.append(line);
	    }

	    // Parse the JSON payload
	    JsonObject jsonObject = JsonParser.parseString(jsonPayload.toString()).getAsJsonObject();

	    // Extract values from the JSON
	    int courseId = jsonObject.get("courseid").getAsInt();
	    int userId = jsonObject.get("userid").getAsInt();
	    int moduleNumber = jsonObject.get("module_number").getAsInt();
	    int totalMarks = jsonObject.get("total_marks").getAsInt();
	    int userMarks = jsonObject.get("user_marks").getAsInt();
	    String result = jsonObject.get("result").getAsString();

	    // Business logic
	    UpdateTestResultDAO dao = new UpdateTestResultDAO();
	    boolean success = dao.updateTestResult(courseId, userId, moduleNumber, totalMarks, userMarks, result);

	    // Create JSON response
	    JsonObject jsonResponse = new JsonObject();
	    if (success) {
	        jsonResponse.addProperty("status", "success");
	        jsonResponse.addProperty("message", "Record inserted/updated successfully.");
	    } else {
	        jsonResponse.addProperty("status", "failure");
	        jsonResponse.addProperty("message", "Higher marks already exist. No changes made.");
	    }

	    // Send response
	    response.getWriter().write(jsonResponse.toString());
	}
}
