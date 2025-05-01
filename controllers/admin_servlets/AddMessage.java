package com.skillbuilders.admin_servlets;

import com.skillbuilders.dao.InstructorMessageDAO;
import com.skillbuilders.util.DBConnection;
import com.skillbuilders.util.InstructorMessage;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/addmessage")
public class AddMessage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AddMessage() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        JsonObject jsonResponse = new JsonObject();
        
        Connection connection = null;
        try {
			connection = DBConnection.getConnection();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        try {
            // Parse the incoming JSON
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                sb.append(line);
            }
            String jsonInput = sb.toString();
            JsonObject jsonObject = com.google.gson.JsonParser.parseString(jsonInput).getAsJsonObject();

            
            int courseId = jsonObject.get("courseId").getAsInt();
            int instructorId = fetchInstructorId(courseId, connection);
            String name = jsonObject.get("name").getAsString();
            String message = jsonObject.get("message").getAsString();

            // Create a message object
            InstructorMessage msg = new InstructorMessage(0, instructorId, courseId, name, message, "false");
            System.out.println(instructorId + " " + courseId + " " + name + " " + message);

            // Call the DAO method to insert the message
            InstructorMessageDAO dao = new InstructorMessageDAO();
            int messageId = dao.addMessage(msg);

            if (messageId > 0) {
                jsonResponse.addProperty("result", "success");
                jsonResponse.addProperty("message", "Message added successfully.");
                jsonResponse.addProperty("messageId", messageId);
            } else {
                jsonResponse.addProperty("result", "failure");
                jsonResponse.addProperty("message", "Failed to add message.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.addProperty("result", "failure");
            jsonResponse.addProperty("message", "An error occurred while adding the message.");
        }

        response.getWriter().write(jsonResponse.toString());
    }
    
    private int fetchInstructorId(int courseId, Connection connection) throws SQLException {
        String query = "SELECT instructorid FROM courses WHERE courseid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, courseId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("instructorid");
                }
            }
        }
        return -1; // Indicates course not found
    }
}
