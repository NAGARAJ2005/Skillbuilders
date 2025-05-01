
package com.skillbuilders.util;

import java.util.*;

public class Course {
    private int courseId;
    private String name;
    private int instructorId;
    private String instructorName;
    private float price;
    private float rating;
    private int ratingCount;
    private float duration;
    private int moduleCount;
    private int enrolledCount;
    private String timeDate;
    private String thumbnail;
    private String description;
    private List<Lecture> lectures;
    private String[] streams;
    private String[] prerequisites;

    public Course(int courseId, String name, int instructorId, String instructorName, float price, float rating, int ratingCount, float duration, int moduleCount, int enrolledCount, String timeDate, String thumbnail, String description, String[] streams, String[] prerequisites) {
        this.courseId = courseId;
        this.name = name;
        this.instructorId = instructorId;
        this.instructorName = instructorName;
        this.price = price;
        this.rating = rating;
        this.ratingCount = ratingCount;
        this.duration = duration;
        this.moduleCount = moduleCount;
        this.enrolledCount = enrolledCount;
        this.timeDate = timeDate;
        this.thumbnail = thumbnail;
        this.description = description;
        this.lectures = new ArrayList<>();
        this.streams = streams;
        this.prerequisites = prerequisites;
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

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }
    
    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public int getModuleCount() {
        return moduleCount;
    }

    public void setModuleCount(int moduleCount) {
        this.moduleCount = moduleCount;
    }

    public int getEnrolledCount() {
        return enrolledCount;
    }

    public void setEnrolledCount(int enrolledCount) {
        this.enrolledCount = enrolledCount;
    }

    public String getTimeDate() {
        return timeDate;
    }

    public void setTimeDate(String timeDate) {
        this.timeDate = timeDate;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Lecture> getLectures() {
        return lectures;
    }

    public void addLecture(Lecture lecture) {
        lectures.add(lecture);
    }

    public String[] getStreams() {
        return streams;
    }

    public void setStreams(String[] streams) {
        this.streams = streams;
    }

    public String[] getPrerequisites() {
        return prerequisites;
    }
    

    public void setPrerequisites(String[] prerequisites) {
        this.prerequisites = prerequisites;
    }

 

@Override
public String toString() {
    return "Course{" +
            "courseId=" + courseId +
            ", name='" + name + '\'' +
            ", instructorId=" + instructorId +
            ", instructorName=" + instructorName +
            ", price=" + price +
            ", rating=" + rating +
            ", ratingCount=" + ratingCount +
            ", duration=" + duration +
            ", moduleCount=" + moduleCount +
            ", enrolledCount=" + enrolledCount +
            ", timeDate='" + timeDate + '\'' +
            ", thumbnail='" + thumbnail + '\'' +
            ", description='" + description + '\'' +
            ", lectures=" + lectures +
            ", streams=" + Arrays.toString(streams) +
            ", prerequisites=" + Arrays.toString(prerequisites) +
            '}';
}

}
