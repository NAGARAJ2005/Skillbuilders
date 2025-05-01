package com.skillbuilders.instructor_servlets;

import com.skillbuilders.dao.InstructorMessageDAO;
import com.skillbuilders.util.InstructorMessage;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/getmessages")
public class GetMessages extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public GetMessages() {
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

            int instructorId = jsonObject.get("instructorId").getAsInt();

            // Call the DAO method to get messages
            InstructorMessageDAO dao = new InstructorMessageDAO();
            List<InstructorMessage> messages = dao.getMessagesByInstructorId(instructorId);

            JsonArray messagesArray = new JsonArray();
            for (InstructorMessage msg : messages) {
                JsonObject messageObj = new JsonObject();
                messageObj.addProperty("messageId", msg.getMessageId());
                messageObj.addProperty("instructorId", msg.getInstructorId());
                messageObj.addProperty("courseId", msg.getCourseId());
                messageObj.addProperty("name", msg.getName());
                messageObj.addProperty("message", msg.getMessage());
                messageObj.addProperty("viewed", msg.getViewed());
                messagesArray.add(messageObj);
            }

            jsonResponse.add("messages", messagesArray);
            jsonResponse.addProperty("result", "success");

        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.addProperty("result", "failure");
            jsonResponse.addProperty("message", "An error occurred while retrieving the messages.");
        }

        response.getWriter().write(jsonResponse.toString());
    }
}
