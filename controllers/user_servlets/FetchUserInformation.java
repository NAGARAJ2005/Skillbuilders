package com.skillbuilders.user_servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.skillbuilders.dao.FetchUserInformationDAO;
import com.skillbuilders.util.UserInformation;

@WebServlet("/fetchuserinformation")
public class FetchUserInformation extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public FetchUserInformation() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        JsonObject jsonResponse = new JsonObject();

        try {
            // Retrieve the current session
            HttpSession session = request.getSession(false); // Get existing session, don't create a new one
            if (session != null && session.getAttribute("userid") != null) {
                int userId = (Integer) session.getAttribute("userid");

                // Fetch user data using DAO
                FetchUserInformationDAO dao = new FetchUserInformationDAO();
                UserInformation userInfo = dao.getUserInformation(userId);

                // Add the user data to the JSON response
                jsonResponse.addProperty("profile", userInfo.getProfile());
                jsonResponse.addProperty("name", userInfo.getName());
                jsonResponse.addProperty("gender", userInfo.getGender());
                jsonResponse.addProperty("professionalSummary", userInfo.getProfessionalSummary());
                jsonResponse.addProperty("dob", userInfo.getDob());
                jsonResponse.addProperty("phoneNumber", userInfo.getPhoneNumber());
                jsonResponse.addProperty("country", userInfo.getCountry());
                jsonResponse.addProperty("city", userInfo.getCity());
                jsonResponse.addProperty("grade", userInfo.getGrade());
                jsonResponse.addProperty("stream", userInfo.getStream());

                // Add interested streams
                JsonArray interestedStreams = new JsonArray();
                for (String stream : userInfo.getInterestedStreams()) {
                    interestedStreams.add(stream);
                }
                jsonResponse.add("interestedStreams", interestedStreams);

                jsonResponse.addProperty("result", "success");
            } else {
                jsonResponse.addProperty("result", "failure");
                jsonResponse.addProperty("message", "User is not logged in or session has expired.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.addProperty("result", "failure");
            jsonResponse.addProperty("message", "An error occurred while fetching user data.");
        }

        response.getWriter().write(jsonResponse.toString());
    }
}
