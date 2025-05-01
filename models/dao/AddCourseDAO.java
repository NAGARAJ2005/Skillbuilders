package com.skillbuilders.dao;

import com.skillbuilders.util.*;
import com.skillbuilders.util.DBConnection;
import com.skillbuilders.util.Lecture;
import com.skillbuilders.util.Question;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddCourseDAO {

    private Connection connection = null;
    private String courseId = "";
    


    public boolean insertCourse(Course course) throws ClassNotFoundException, SQLException {
        connection = DBConnection.getConnection();
        if (connection == null) {
            System.err.println("Database connection failed.");
            return false;
        }

        String query = "INSERT INTO courses (name, instructorid, price, rating, rating_count, duration, module_count, enrolled_count, thumbnail, description) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, course.getName());
            stmt.setInt(2, course.getInstructorId());
            stmt.setFloat(3, course.getPrice());
            stmt.setFloat(4, course.getRating());
            stmt.setInt(5, course.getRatingCount());
            stmt.setFloat(6, course.getDuration());
            stmt.setInt(7, course.getModuleCount());
            stmt.setInt(8, course.getEnrolledCount());
            stmt.setString(9, course.getThumbnail());
            stmt.setString(10, course.getDescription());

            if (stmt.executeUpdate() > 0) {
                courseId = getMaxCourseId();
                if (courseId != null) {
                    System.out.println("Course inserted successfully. Course ID: " + courseId);
                    return true;
                } else {
                    System.err.println("Failed to retrieve Course ID after insertion.");
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertCourseStreams(Course course) throws ClassNotFoundException, SQLException{
    	connection = DBConnection.getConnection();
        if (connection == null) {
            System.err.println("Database connection is null in insertCourseStreams.");
            return false;
        }

        String query = "INSERT INTO coursestreams (courseid, stream) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (String stream : course.getStreams()) {
                stmt.setString(1, courseId);
                stmt.setString(2, stream);
                stmt.addBatch();
            }
            stmt.executeBatch();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            deleteCourse(courseId);
            return false;
        }
    }

    public boolean insertCoursePrerequisites(Course course) throws ClassNotFoundException, SQLException {
    	connection = DBConnection.getConnection();
        if (connection == null) {
            System.err.println("Database connection is null in insertCoursePrerequisites.");
            return false;
        }

        String query = "INSERT INTO courseprerequisites (courseid, prerequisite) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (String prerequisite : course.getPrerequisites()) {
                stmt.setString(1, courseId);
                stmt.setString(2, prerequisite);
                stmt.addBatch();
            }
            stmt.executeBatch();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            deleteCourse(courseId);
            return false;
        }
    }

    public boolean insertCourseLectures(Course course) throws ClassNotFoundException, SQLException {
        connection = DBConnection.getConnection();
        if (connection == null) {
            System.err.println("Database connection is null in insertCourseLectures.");
            return false;
        }

        String lectureQuery = "INSERT INTO lectures (courseid, module_number, module_name, link) VALUES (?, ?, ?, ?)";
        String questionQuery = "INSERT INTO questions (question_number, question, option1, option2, option3, option4, answer, courseid, module_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement lectureStmt = connection.prepareStatement(lectureQuery);
             PreparedStatement questionStmt = connection.prepareStatement(questionQuery)) {

            // Inserting lectures and questions
            for (Lecture lecture : course.getLectures()) {
                lectureStmt.setString(1, courseId);
                lectureStmt.setInt(2, lecture.getModuleNumber());
                lectureStmt.setString(3, lecture.getModuleName());
                lectureStmt.setString(4, lecture.getLink());
                lectureStmt.executeUpdate(); // Execute the insert for the lecture

                for (Question question : lecture.getQuestions()) {
                    questionStmt.setInt(1, question.getQuestionNumber());
                    questionStmt.setString(2, question.getQuestion());
                    questionStmt.setString(3, question.getOption1());
                    questionStmt.setString(4, question.getOption2());
                    questionStmt.setString(5, question.getOption3());
                    questionStmt.setString(6, question.getOption4());
                    questionStmt.setString(7, question.getAnswer());
                    questionStmt.setString(8, courseId);
                    questionStmt.setInt(9, lecture.getModuleNumber());
                    questionStmt.executeUpdate(); // Execute the insert for the question
                }
            }

            System.out.println("Course lectures and questions inserted successfully.");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            // If any error occurs, delete the course from the database
            deleteCourse(courseId);
            return false;
        }
    }

    
    public boolean deleteCourse(String courseid) throws ClassNotFoundException, SQLException {
        connection = DBConnection.getConnection();
        if (connection == null) {
            System.err.println("Database connection failed in deleteCourse.");
            return false;
        }

        String deleteQuery = "DELETE FROM courses WHERE courseid = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteQuery)) {
            stmt.setString(1, courseid);
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Course deleted successfully. Course ID: " + courseId);
                return true;
            } else {
                System.err.println("No course found with Course ID: " + courseId);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean approveCourse(int courseId) throws ClassNotFoundException {
        String query = "UPDATE courses SET approved = 'true' WHERE courseid = ?";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setInt(1, courseId);
            int rowsAffected = stmt.executeUpdate();
            
            return rowsAffected > 0;  // Return true if the course was updated
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Return false if any exception occurs
        }
    }


    public String getMaxCourseId() throws ClassNotFoundException, SQLException{
    	connection = DBConnection.getConnection();
        System.out.println("Retrieving maximum course ID...");
        String query = "SELECT MAX(courseid) AS max_course_id FROM courses";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return String.valueOf(rs.getInt("max_course_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error while retrieving the maximum course ID.");
        }
        return null;
    }
    
}
