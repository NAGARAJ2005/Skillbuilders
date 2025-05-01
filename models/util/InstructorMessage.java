package com.skillbuilders.util;

public class InstructorMessage {
    private int messageId;
    private int instructorId;
    private int courseId;
    private String name;
    private String message;
    private String viewed;

    // Constructor
    public InstructorMessage(int messageId, int instructorId, int courseId, String name, String message, String viewed) {
        this.messageId = messageId;
        this.instructorId = instructorId;
        this.courseId = courseId;
        this.name = name;
        this.message = message;
        this.viewed = viewed;
    }

    // Getters and Setters
    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getViewed() {
        return viewed;
    }

    public void setViewed(String viewed) {
        this.viewed = viewed;
    }
}

