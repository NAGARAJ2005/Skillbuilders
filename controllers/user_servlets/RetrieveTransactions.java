package com.skillbuilders.user_servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.skillbuilders.util.DBConnection;

@WebServlet("/retrievetransactions")
public class RetrieveTransactions extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Set response content type
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        // Get the userId parameter from the request
        String userId = null;
        HttpSession session = request.getSession(false); // Pass `false` to avoid creating a new session if none exists

        if (session != null) {
            // Retrieve the userid attribute
            Object userIdObj = session.getAttribute("userid");

            if (userIdObj != null) {
                // If userId is not null, convert it to a string
                userId = userIdObj.toString();
            } else {
                response.getWriter().println("User ID not found in session.");
                return;
            }
        } else {
            response.getWriter().println("No active session found.");
            return;
        }

        
        if (userId == null || userId.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\": \"Missing userid parameter\"}");
            return;
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Obtain database connection
            connection = DBConnection.getConnection();

            // SQL query to fetch transactions for the given userId
            String query = "SELECT transactionid, amount, courseid, details, time_date FROM transactions WHERE userid = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(userId));

            resultSet = preparedStatement.executeQuery();

            // Create a JsonArray to store the transaction objects
            JsonArray transactionsArray = new JsonArray();

            while (resultSet.next()) {
                // Create a JsonObject for each transaction
                JsonObject transaction = new JsonObject();
                transaction.addProperty("transactionid", resultSet.getInt("transactionid"));
                transaction.addProperty("amount", resultSet.getFloat("amount"));
                transaction.addProperty("courseid", resultSet.getInt("courseid"));
                transaction.addProperty("details", resultSet.getString("details"));
                transaction.addProperty("time_date", resultSet.getTimestamp("time_date").toString());

                // Add the transaction to the JsonArray
                transactionsArray.add(transaction);
            }

            // Write the JSON response using Gson's toString method
            out.write(transactionsArray.toString());
            System.out.println(transactionsArray);
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"error\": \"An error occurred while fetching transactions.\"}");
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
