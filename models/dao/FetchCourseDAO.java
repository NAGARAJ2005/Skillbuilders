package com.skillbuilders.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.skillbuilders.util.*;

public class FetchCourseDAO {

    public Course getCourseDetails(int courseId, Connection connection) throws SQLException {
        Course course = null;

        // Fetch course details from the 'courses' table
        String query = "SELECT * FROM courses WHERE courseid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, courseId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int instructorId = resultSet.getInt("instructorid");
                    String instructorName = getInstructorName(instructorId, connection);

                    course = new Course(
                            resultSet.getInt("courseid"),
                            resultSet.getString("name"),
                            instructorId,
                            instructorName, // Replace placeholder with fetched instructor name
                            resultSet.getFloat("price"),
                            resultSet.getFloat("rating"),
                            resultSet.getInt("rating_count"),
                            resultSet.getFloat("duration"),
                            resultSet.getInt("module_count"),
                            resultSet.getInt("enrolled_count"),
                            resultSet.getString("time_date"),
                            resultSet.getString("thumbnail"),
                            resultSet.getString("description"),
                            null, // Streams and prerequisites will be set later
                            null
                    );
                }
            }
        }

        // Fetch and set streams and prerequisites
        if (course != null) {
            course.setStreams(getStreams(courseId, connection));
            course.setPrerequisites(getPrerequisites(courseId, connection));
            List<Lecture> lectures = getLectures(courseId, connection);
            for (Lecture lecture : lectures) {
                course.addLecture(lecture);
            }
        }

        return course;
    }

    public String getInstructorName(int instructorId, Connection connection) throws SQLException {
        String instructorName = null;
        String query = "SELECT name FROM instructors WHERE instructorid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, instructorId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    instructorName = resultSet.getString("name");
                }
            }
        }
        return instructorName != null ? instructorName : "Unknown Instructor";
    }

    public String[] getStreams(int courseId, Connection connection) throws SQLException {
        List<String> streams = new ArrayList<>();
        String query = "SELECT stream FROM coursestreams WHERE courseid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, courseId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    streams.add(resultSet.getString("stream"));
                }
            }
        }
        return streams.toArray(new String[0]);
    }

    public String[] getPrerequisites(int courseId, Connection connection) throws SQLException {
        List<String> prerequisites = new ArrayList<>();
        String query = "SELECT prerequisite FROM courseprerequisites WHERE courseid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, courseId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    prerequisites.add(resultSet.getString("prerequisite"));
                }
            }
        }
        return prerequisites.toArray(new String[0]);
    }

    public List<Lecture> getLectures(int courseId, Connection connection) throws SQLException {
        List<Lecture> lectures = new ArrayList<>();
        String query = "SELECT * FROM lectures WHERE courseid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, courseId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Lecture lecture = new Lecture(
                            resultSet.getInt("courseid"),
                            resultSet.getInt("module_number"),
                            resultSet.getString("module_name"),
                            resultSet.getString("link")
                    );
                    // Fetch and set questions for each lecture
                    List<Question> questions = getQuestions(courseId, lecture.getModuleNumber(), connection);
                    for (Question question : questions) {
                        lecture.addQuestion(question);
                    }
                    lectures.add(lecture);
                }
            }
        }
        return lectures;
    }

    public List<Question> getQuestions(int courseId, int moduleNumber, Connection connection) throws SQLException {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT * FROM questions WHERE courseid = ? AND module_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, courseId);
            statement.setInt(2, moduleNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Question question = new Question(
                            resultSet.getInt("question_number"),
                            resultSet.getString("question"),
                            resultSet.getString("option1"),
                            resultSet.getString("option2"),
                            resultSet.getString("option3"),
                            resultSet.getString("option4"),
                            resultSet.getString("answer"),
                            resultSet.getInt("courseid"),
                            resultSet.getInt("module_number")
                    );
                    questions.add(question);
                }
            }
        }
        return questions;
    }
}
