	package com.skillbuilders.user_servlets;
	
	import com.google.gson.Gson;
	import com.google.gson.JsonObject;
	import com.skillbuilders.dao.InsertUserCoursesDAO;
	
	import javax.servlet.ServletException;
	import javax.servlet.annotation.WebServlet;
	import javax.servlet.http.HttpServlet;
	import javax.servlet.http.HttpServletRequest;
	import javax.servlet.http.HttpServletResponse;
	import java.io.BufferedReader;
	import java.io.IOException;
	import java.sql.SQLException;

@WebServlet("/addtofavourites")
public class AddToFavourites extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        try {
            // Parse the incoming JSON from the request body
            BufferedReader reader = request.getReader();
            StringBuilder jsonInput = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonInput.append(line);
            }

            // Convert the JSON string into a JsonObject
            JsonObject jsonObject = gson.fromJson(jsonInput.toString(), JsonObject.class);

            // Extract userId, courseId, and courseType from the request JSON
            int userId = jsonObject.get("userid").getAsInt();
            int courseId = jsonObject.get("courseid").getAsInt();
            String courseType = jsonObject.get("course_type").getAsString();
            System.out.println("userId : " + userId);

            // Create an instance of InsertUserCoursesDAO to interact with the database
            InsertUserCoursesDAO dao = new InsertUserCoursesDAO();
            boolean success = dao.addCourseToUser(userId, courseId, courseType);

            // Prepare the response based on success or failure
            String responseMessage;
            if (success) {
                responseMessage = "{\"status\": \"success\", \"message\": \"Course added to favourites successfully!\"}";
            } else {
                responseMessage = "{\"status\": \"error\", \"message\": \"Course already exists in your favourites.\"}";
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            response.getWriter().write(responseMessage);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            //response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\": \"error\", \"message\": \"You have already added this course.\"}");
        }
    }
}
