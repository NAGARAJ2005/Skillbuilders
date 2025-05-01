package com.skillbuilders.servlets.course_servlets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.skillbuilders.dao.FetchCourseDAO;
import com.skillbuilders.util.Course;
import com.skillbuilders.util.DBConnection;
import com.skillbuilders.util.Lecture;
import com.skillbuilders.util.Question;

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

@WebServlet("/previewcourses")
public class PreviewCourses extends HttpServlet {


	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JsonArray coursesJsonArray = new JsonArray();

        try (Connection connection = DBConnection.getConnection()) {
            // Query to get course IDs with approved = false
            String query = "SELECT courseid FROM courses WHERE approved = 'false'";
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {

                FetchCourseDAO fetchCourseDAO = new FetchCourseDAO();

                // Fetch details for each course and convert to JSON
                while (resultSet.next()) {
                    int courseId = resultSet.getInt("courseid");
                    try {
                        Course course = fetchCourseDAO.getCourseDetails(courseId, connection);
                        if (course != null) {
                            coursesJsonArray.add(convertCourseToJson(course));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        // Send JSON array as response
        response.getWriter().write(coursesJsonArray.toString());
    }

    private JsonObject convertCourseToJson(Course course) {
        JsonObject json = new JsonObject();
        json.addProperty("courseId", course.getCourseId());
        json.addProperty("name", course.getName());
        json.addProperty("instructorId", course.getInstructorId());
        json.addProperty("price", course.getPrice());
        json.addProperty("rating", course.getRating());
        json.addProperty("ratingCount", course.getRatingCount());
        json.addProperty("duration", course.getDuration());
        json.addProperty("moduleCount", course.getModuleCount());
        json.addProperty("enrolledCount", course.getEnrolledCount());
        json.addProperty("timeDate", course.getTimeDate());
        json.addProperty("thumbnail", course.getThumbnail());
        json.addProperty("description", course.getDescription());

        // Add streams
        JsonArray streams = new JsonArray();
        for (String stream : course.getStreams()) {
            streams.add(stream);
        }
        json.add("streams", streams);

        // Add prerequisites
        JsonArray prerequisites = new JsonArray();
        for (String prerequisite : course.getPrerequisites()) {
            prerequisites.add(prerequisite);
        }
        json.add("prerequisites", prerequisites);

        // Add lectures
        JsonArray lectures = new JsonArray();
        for (Lecture lecture : course.getLectures()) {
            JsonObject lectureJson = new JsonObject();
            lectureJson.addProperty("moduleNumber", lecture.getModuleNumber());
            lectureJson.addProperty("moduleName", lecture.getModuleName());
            lectureJson.addProperty("link", lecture.getLink());

            // Add questions
            JsonArray questions = new JsonArray();
            for (Question question : lecture.getQuestions()) {
                JsonObject questionJson = new JsonObject();
                questionJson.addProperty("questionNumber", question.getQuestionNumber());
                questionJson.addProperty("question", question.getQuestion());
                questionJson.addProperty("option1", question.getOption1());
                questionJson.addProperty("option2", question.getOption2());
                questionJson.addProperty("option3", question.getOption3());
                questionJson.addProperty("option4", question.getOption4());
                questionJson.addProperty("answer", question.getAnswer());
                questions.add(questionJson);
            }
            lectureJson.add("questions", questions);
            lectures.add(lectureJson);
        }
        json.add("lectures", lectures);

        return json;
    }
}
