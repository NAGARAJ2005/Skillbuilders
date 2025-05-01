package com.skillbuilders.user_servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.skillbuilders.dao.UpdateUserInformationDAO;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/updateuserinformation")
public class UpdateUserInformation extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Setting response type
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        System.out.println("Inside update servlet...");

        String userId = request.getParameter("userid"); // Get the userId from the request
        String field = request.getParameter("field");   // The field to update
        String newValue = request.getParameter("value"); // The new value for the field
        
        // Check if required parameters are present
        if (userId == null || field == null || newValue == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Missing required parameters\"}");
            return;
        }
        
        
        String[] interestedStreams = null;
        if (!"interestedStreams".equals(field)) 
        	newValue = request.getParameter("value"); // New value for the field
        else  {
            String value = request.getParameter("value");
	        if (value != null && !value.isEmpty()) {
	            // Split the string by commas and trim spaces
	            interestedStreams = value.split("\\s*,\\s*");
	        }
        }

        System.out.println("Request : " + field);
        System.out.println("Value : " + newValue);
        System.out.println("Value : " + request.getParameter("value"));
        UpdateUserInformationDAO userProfileDAO = new UpdateUserInformationDAO();
        boolean success = false;

        try {
            if (field != null && (newValue != null || interestedStreams!=null)) {
                switch (field.toLowerCase()) {
                    case "name":
                        success = userProfileDAO.updateUsername(userId, newValue);
                        break;
                    case "gender":
                        success = userProfileDAO.updateGender(userId, newValue);
                        break;
                    case "professionalsummary":
                        success = userProfileDAO.updateSummary(userId, newValue);
                        break;
                    case "dob":
                        success = userProfileDAO.updateDOB(userId, newValue);
                        break;
                    case "phonenumber":
                        success = userProfileDAO.updatePhoneNumber(userId, newValue);
                        break;
                    case "country":
                        success = userProfileDAO.updateCountry(userId, newValue);
                        break;
                    case "city":
                        success = userProfileDAO.updateCity(userId, newValue);
                        break;
                    case "grade":
                        success = userProfileDAO.updateGrade(userId, newValue);
                        break;
                    case "stream":
                        success = userProfileDAO.updateCurrentStream(userId, newValue);
                        break;
                    case "profile":
                        success = userProfileDAO.updateCurrentProfile(userId, newValue);
                        break;
                    case "interestedstreams":
                        success = userProfileDAO.updateInterestedStreams(userId, interestedStreams);
                        break;                    	
                    default:
                        throw new IllegalArgumentException("Invalid field: " + field);
                }
            }
               

            if (success) {
                out.write("{\"status\": \"success\", \"message\": \"Update successful.\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"status\": \"failure\", \"message\": \"Update failed.\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"status\": \"error\", \"message\": \"An error occurred: " + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }
}
