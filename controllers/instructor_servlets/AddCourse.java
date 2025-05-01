package com.skillbuilders.instructor_servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.skillbuilders.dao.AddCourseDAO;
import com.skillbuilders.util.Course;
import com.skillbuilders.util.Lecture;
import com.skillbuilders.util.Question;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

@WebServlet("/addcourse")
public class AddCourse extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final Gson gson = new Gson();
	private Integer instructorId = null;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    response.setContentType("application/json");
	    
	    HttpSession session = request.getSession(false); // false to not create a new session if one doesn't exist

	    // Check if the session is null or if the instructorId is not found in the session
	    if (session == null || session.getAttribute("instructorid") == null) {
	        // If the session is not found or instructorId is missing, return error
	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Set appropriate HTTP status code
	        response.getWriter().write("{\"status\": \"error\", \"message\": \"Session has ended\"}");
	        return;
	    }
	    
	    instructorId = (Integer) session.getAttribute("instructorid");

	    
	    try {
	        // Parse JSON from the request body
	        BufferedReader reader = request.getReader();
	        StringBuilder jsonInput = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) {
	            jsonInput.append(line);
	        }

	        // Convert JSON string to a JsonObject
	        JsonObject jsonObject = gson.fromJson(jsonInput.toString(), JsonObject.class);

	        // Process the JSON and create a Course object
	        Course course = getCourseObject(jsonObject);

	        // Prepare the response to include course creation details
	        StringBuilder responseMessage = new StringBuilder();

	        // Database DAO setup
	        AddCourseDAO dao = new AddCourseDAO();
	        boolean success = true;

	        try {
	            // Insert course data into the database
	            if (!dao.insertCourse(course)) {
	                success = false;
	                responseMessage.append("{\"status\": \"error\", \"message\": \"Error in db - dao\"}");
	            } else if (!dao.insertCourseStreams(course)) {
	                success = false;
	                responseMessage.append("{\"status\": \"error\", \"message\": \"Error in db - coursestreams\"}");
	            } else if (!dao.insertCoursePrerequisites(course)) {
	                success = false;
	                responseMessage.append("{\"status\": \"error\", \"message\": \"Error in db - courseprerequisites\"}");
	            } else if (!dao.insertCourseLectures(course)) {
	                success = false;
	                responseMessage.append("{\"status\": \"error\", \"message\": \"Error in db - courselectures\"}");
	            }

	            if (success) {
	                responseMessage.append("{\"status\": \"success\", \"message\": \"Course data processed successfully!\"}");
	                response.getWriter().write(responseMessage.toString());
	            } else {
	                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	                response.getWriter().write(responseMessage.toString());
	            }

	        } catch (Exception e) {
	            // Handle exceptions and clean up by deleting the course if any exception occurs
	            e.printStackTrace();
	            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	            response.getWriter().write("{\"status\": \"error\", \"message\": \"Invalid data format or processing error.\"}");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	        response.getWriter().write("{\"status\": \"error\", \"message\": \"Invalid data format or processing error.\"}");
	    }
	}


    /**
     * Parses the JSON object to create a Course object with nested modules, lectures, and questions.
     */
    private Course getCourseObject(JsonObject jsonObject) {
        JsonObject courseDetails = jsonObject.getAsJsonObject("courseDetails");

        // Extract basic course details with null checks
        String title = courseDetails.has("title") && !courseDetails.get("title").isJsonNull() 
                       ? courseDetails.get("title").getAsString() 
                       : ""; // Default to empty string if null or missing
        String description = courseDetails.has("description") && !courseDetails.get("description").isJsonNull() 
                             ? courseDetails.get("description").getAsString() 
                             : "";
        float duration = courseDetails.has("duration") && !courseDetails.get("duration").isJsonNull() 
                         ? courseDetails.get("duration").getAsFloat() 
                         : 0.0f;
        int moduleCount = courseDetails.has("modulesCount") && !courseDetails.get("modulesCount").isJsonNull() 
                          ? courseDetails.get("modulesCount").getAsInt() 
                          : 0;
        String thumbnail = courseDetails.has("thumbnail") && !courseDetails.get("thumbnail").isJsonNull() 
                           ? courseDetails.get("thumbnail").getAsString() 
                           : "";
        float price = courseDetails.has("price") && !courseDetails.get("price").isJsonNull() 
                      ? courseDetails.get("price").getAsFloat() 
                      : 0.0f;
        String[] streams = gson.fromJson(courseDetails.getAsJsonArray("streams"), String[].class);
        String[] prerequisites = gson.fromJson(courseDetails.getAsJsonArray("prerequisites"), String[].class);

        Course course = new Course(0, title, instructorId, "Instructor", price, 0, 0, duration, moduleCount, 0, null, thumbnail, description, streams, prerequisites);

        // Extract modules
        Type moduleListType = new TypeToken<List<JsonObject>>() {}.getType();
        List<JsonObject> modules = gson.fromJson(jsonObject.getAsJsonArray("modules"), moduleListType);

        int moduleNumber = 1;
        for (JsonObject moduleJson : modules) {
            String moduleName = moduleJson.has("moduleName") && !moduleJson.get("moduleName").isJsonNull() 
                                ? moduleJson.get("moduleName").getAsString() 
                                : null;
            String lectureLink = moduleJson.has("lectureLink") && !moduleJson.get("lectureLink").isJsonNull() 
                                 ? moduleJson.get("lectureLink").getAsString() 
                                 : null;

            Lecture lecture = new Lecture(0, moduleNumber++, moduleName, lectureLink);

            // Extract and add questions
            if (moduleJson.has("questions") && !moduleJson.get("questions").isJsonNull()) {
                Type questionListType = new TypeToken<List<JsonObject>>() {}.getType();
                List<JsonObject> questions = gson.fromJson(moduleJson.getAsJsonArray("questions"), questionListType);
                
                int qNum = 1;
                for (JsonObject questionJson : questions) {
                    String questionText = questionJson.has("questionText") && !questionJson.get("questionText").isJsonNull() 
                                          ? questionJson.get("questionText").getAsString() 
                                          : "";
                    String option1 = questionJson.has("options") && questionJson.getAsJsonArray("options").size() > 0 
                                     ? questionJson.getAsJsonArray("options").get(0).getAsString() 
                                     : "";
                    String option2 = questionJson.has("options") && questionJson.getAsJsonArray("options").size() > 1 
                                     ? questionJson.getAsJsonArray("options").get(1).getAsString() 
                                     : "";
                    String option3 = questionJson.has("options") && questionJson.getAsJsonArray("options").size() > 2 
                                     ? questionJson.getAsJsonArray("options").get(2).getAsString() 
                                     : "";
                    String option4 = questionJson.has("options") && questionJson.getAsJsonArray("options").size() > 3 
                                     ? questionJson.getAsJsonArray("options").get(3).getAsString() 
                                     : "";
                    String correctOption = questionJson.has("correctOption") && !questionJson.get("correctOption").isJsonNull() 
                                           ? questionJson.get("correctOption").getAsString() 
                                           : "";

                    Question question = new Question(qNum++, questionText, option1, option2, option3, option4, correctOption, 0, moduleNumber);
                    lecture.addQuestion(question);
                }
            }

            course.addLecture(lecture);
        }

        return course;
    }


    /*private void printCourseDetails(Course course) {
        System.out.println("Course Name: " + course.getName());
        System.out.println("Description: " + course.getDescription());
        System.out.println("Duration: " + course.getDuration() + " hours");
        System.out.println("Modules Count: " + course.getModuleCount());
        System.out.println("Price: $" + course.getPrice());
        System.out.println("Streams: " + String.join(", ", course.getStreams()));
        System.out.println("Prerequisites: " + String.join(", ", course.getPrerequisites()));

        System.out.println("Modules:");
        for (Lecture lecture : course.getLectures()) {
            System.out.println("  Module Name: " + lecture.getModuleName());
            System.out.println("  Lecture Link: " + lecture.getLink());

            System.out.println("  Questions:");
            for (Question question : lecture.getQuestions()) {
                System.out.println("    Question: " + question.getQuestion());
                System.out.println("    Options: [" + question.getOption1() + ", " + question.getOption2() + ", " +
                        question.getOption3() + ", " + question.getOption4() + "]");
                System.out.println("    Correct Option: " + question.getAnswer());
            }
        }
    }*/
}
