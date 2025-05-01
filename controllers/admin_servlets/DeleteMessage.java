package com.skillbuilders.admin_servlets;

import com.skillbuilders.dao.InstructorMessageDAO;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/deletemessage")
public class DeleteMessage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public DeleteMessage() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        JsonObject jsonResponse = new JsonObject();

        try {
            // Parse the incoming JSON
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                sb.append(line);
            }
            String jsonInput = sb.toString();
            JsonObject jsonObject = com.google.gson.JsonParser.parseString(jsonInput).getAsJsonObject();

            int messageId = jsonObject.get("messageId").getAsInt();

            // Call the DAO method to delete the message
            InstructorMessageDAO dao = new InstructorMessageDAO();
            boolean isDeleted = dao.deleteMessage(messageId);

            if (isDeleted) {
                jsonResponse.addProperty("result", "success");
                jsonResponse.addProperty("message", "Message deleted successfully.");
            } else {
                jsonResponse.addProperty("result", "failure");
                jsonResponse.addProperty("message", "Failed to delete message.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.addProperty("result", "failure");
            jsonResponse.addProperty("message", "An error occurred while deleting the message.");
        }

        response.getWriter().write(jsonResponse.toString());
    }
}
