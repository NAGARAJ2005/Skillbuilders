package com.skillbuilders.util;

import java.util.*;

public class Lecture {
    private int courseId;
    private int moduleNumber;
    private String moduleName;
    private String link;
    private List<Question> questions;

    public Lecture(int courseId, int moduleNumber, String moduleName, String link) {
        this.courseId = courseId;
        this.moduleNumber = moduleNumber;
        this.moduleName = moduleName;
        this.link = link;
        this.questions = new ArrayList<>();
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getModuleNumber() {
        return moduleNumber;
    }

    public void setModuleNumber(int moduleNumber) {
        this.moduleNumber = moduleNumber;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    @Override
    public String toString() {
        return "Lecture{" +
                "courseId=" + courseId +
                ", moduleNumber=" + moduleNumber +
                ", moduleName='" + moduleName + '\'' +
                ", link='" + link + '\'' +
                ", questions=" + questions +
                '}';
    }
}
