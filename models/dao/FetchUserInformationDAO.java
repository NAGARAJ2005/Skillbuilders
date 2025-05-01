package com.skillbuilders.dao;

import com.skillbuilders.util.DBConnection;
import com.skillbuilders.util.UserInformation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FetchUserInformationDAO {

    public UserInformation getUserInformation(int userId) throws SQLException, ClassNotFoundException {
        UserInformation userInformation = new UserInformation();

        // Establish a single connection for the entire operation
        try (Connection connection = DBConnection.getConnection()) {

            // Fetch user details
            String userQuery = "SELECT * FROM users WHERE userid = ?";
            try (PreparedStatement userStmt = connection.prepareStatement(userQuery)) {
                userStmt.setInt(1, userId);
                ResultSet userRs = userStmt.executeQuery();
                if (userRs.next()) {
                    userInformation.setUserid(userRs.getInt("userid"));
                    userInformation.setName(userRs.getString("name"));
                    userInformation.setGender(userRs.getString("gender"));
                    userInformation.setEmail(userRs.getString("email"));
                    userInformation.setPhoneNumber(userRs.getLong("phone_number"));
                    userInformation.setPassword(userRs.getString("password"));
                    userInformation.setGrade(userRs.getString("grade"));
                    userInformation.setStream(userRs.getString("stream"));
                    userInformation.setCountry(userRs.getString("country"));
                    userInformation.setCity(userRs.getString("city"));
                    userInformation.setProfessionalSummary(userRs.getString("professional_summary"));
                    userInformation.setDob(userRs.getString("DOB"));
                    userInformation.setProfile(userRs.getString("profile"));
                }
            }

            // Fetch interested streams
            String streamsQuery = "SELECT stream FROM userinterestedstream WHERE userid = ?";
            try (PreparedStatement streamStmt = connection.prepareStatement(streamsQuery)) {
                streamStmt.setInt(1, userId);
                ResultSet streamRs = streamStmt.executeQuery();
                List<String> interestedStreams = new ArrayList<>();
                while (streamRs.next()) {
                    interestedStreams.add(streamRs.getString("stream"));
                }
                userInformation.setInterestedStreams(interestedStreams);
            }

            // Fetch cart, favourite, and enrolled courses
            fetchUserCourses(connection, userId, "cart", userInformation);
            fetchUserCourses(connection, userId, "favourite", userInformation);
            fetchUserCourses(connection, userId, "enrolled", userInformation);

            // Fetch certificates
            String certificatesQuery = "SELECT * FROM certificates WHERE userid = ?";
            try (PreparedStatement certStmt = connection.prepareStatement(certificatesQuery)) {
                certStmt.setInt(1, userId);
                ResultSet certRs = certStmt.executeQuery();
                List<UserInformation.Certificate> certificates = new ArrayList<>();
                while (certRs.next()) {
                    UserInformation.Certificate certificate = new UserInformation.Certificate();
                    certificate.setCourseid(certRs.getInt("courseid"));
                    certificate.setPath(certRs.getString("path"));
                    certificates.add(certificate);
                }
                userInformation.setCertificates(certificates);
            }

            // Fetch transactions
            String transactionsQuery = "SELECT * FROM transactions WHERE userid = ?";
            try (PreparedStatement txnStmt = connection.prepareStatement(transactionsQuery)) {
                txnStmt.setInt(1, userId);
                ResultSet txnRs = txnStmt.executeQuery();
                List<UserInformation.Transaction> transactions = new ArrayList<>();
                while (txnRs.next()) {
                    UserInformation.Transaction transaction = new UserInformation.Transaction();
                    transaction.setAmount(txnRs.getFloat("amount"));
                    transaction.setCourseid(txnRs.getInt("courseid"));
                    transaction.setTimeDate(txnRs.getString("time_date"));
                    transactions.add(transaction);
                }
                userInformation.setTransactions(transactions);
            }

            // Fetch test results
            String testResultsQuery = "SELECT * FROM testresult WHERE userid = ?";
            try (PreparedStatement testStmt = connection.prepareStatement(testResultsQuery)) {
                testStmt.setInt(1, userId);
                ResultSet testRs = testStmt.executeQuery();
                List<UserInformation.TestResult> testResults = new ArrayList<>();
                while (testRs.next()) {
                    UserInformation.TestResult testResult = new UserInformation.TestResult();
                    testResult.setCourseid(testRs.getInt("courseid"));
                    testResult.setModuleNumber(testRs.getInt("module_number"));
                    testResult.setTotalMarks(testRs.getInt("total_marks"));
                    testResult.setUserMarks(testRs.getInt("user_marks"));
                    testResult.setResult(testRs.getString("result"));
                    testResults.add(testResult);
                }
                userInformation.setTestResults(testResults);
            }
        }

        return userInformation;
    }

    private void fetchUserCourses(Connection connection, int userId, String courseType, UserInformation userInformation) throws SQLException {
        String coursesQuery = "SELECT * FROM usercourses WHERE userid = ? AND course_type = ?";
        try (PreparedStatement courseStmt = connection.prepareStatement(coursesQuery)) {
            courseStmt.setInt(1, userId);
            courseStmt.setString(2, courseType);
            ResultSet courseRs = courseStmt.executeQuery();
            List<UserInformation.CourseDetails> courseList = new ArrayList<>();
            while (courseRs.next()) {
                UserInformation.CourseDetails course = new UserInformation.CourseDetails();
                course.setCourseid(courseRs.getInt("courseid"));
                course.setCourseType(courseType);
                if ("enrolled".equals(courseType)) {
                    course.setProgress(courseRs.getInt("progress"));
                }
                courseList.add(course);
            }

            switch (courseType) {
                case "cart":
                    userInformation.setCartCourses(courseList);
                    break;
                case "favourite":
                    userInformation.setFavouriteCourses(courseList);
                    break;
                case "enrolled":
                    userInformation.setEnrolledCourses(courseList);
                    break;
            }
        }
    }
}
