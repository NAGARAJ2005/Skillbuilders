package com.skillbuilders.util;

import java.util.List;

public class UserInformation {
    private int userid;
    private String name;
    private String gender;
    private String email;
    private long phoneNumber;
    private String password;
    private String grade;
    private String stream;
    private String country;
    private String city;
    private String professionalSummary;
    private String dob;
    private String profile;

    private List<String> interestedStreams; // List of interested streams
    private List<CourseDetails> cartCourses; // List of cart courses
    private List<CourseDetails> favouriteCourses; // List of favourite courses
    private List<CourseDetails> enrolledCourses; // List of enrolled courses
    private List<Certificate> certificates; // List of certificates
    private List<Transaction> transactions; // List of transactions
    private List<TestResult> testResults; // List of test results

    // Getters and Setters
    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProfessionalSummary() {
        return professionalSummary;
    }

    public void setProfessionalSummary(String professionalSummary) {
        this.professionalSummary = professionalSummary;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public List<String> getInterestedStreams() {
        return interestedStreams;
    }

    public void setInterestedStreams(List<String> interestedStreams) {
        this.interestedStreams = interestedStreams;
    }

    public List<CourseDetails> getCartCourses() {
        return cartCourses;
    }

    public void setCartCourses(List<CourseDetails> cartCourses) {
        this.cartCourses = cartCourses;
    }

    public List<CourseDetails> getFavouriteCourses() {
        return favouriteCourses;
    }

    public void setFavouriteCourses(List<CourseDetails> favouriteCourses) {
        this.favouriteCourses = favouriteCourses;
    }

    public List<CourseDetails> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void setEnrolledCourses(List<CourseDetails> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public List<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<Certificate> certificates) {
        this.certificates = certificates;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<TestResult> getTestResults() {
        return testResults;
    }

    public void setTestResults(List<TestResult> testResults) {
        this.testResults = testResults;
    }

    // Nested class to represent Course Details
    public static class CourseDetails {
        private int courseid;
        private String courseType; // favourite, cart, or enrolled
        private int progress; // Only applicable for enrolled courses

        // Getters and Setters
        public int getCourseid() {
            return courseid;
        }

        public void setCourseid(int courseid) {
            this.courseid = courseid;
        }

        public String getCourseType() {
            return courseType;
        }

        public void setCourseType(String courseType) {
            this.courseType = courseType;
        }

        public int getProgress() {
            return progress;
        }

        public void setProgress(int progress) {
            this.progress = progress;
        }
    }

    // Nested class to represent Certificate details
    public static class Certificate {
        private int courseid;
        private String path; // Path to the certificate

        // Getters and Setters
        public int getCourseid() {
            return courseid;
        }

        public void setCourseid(int courseid) {
            this.courseid = courseid;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    // Nested class to represent Transaction details
    public static class Transaction {
        private float amount;
        private int courseid;
        private String timeDate;

        // Getters and Setters
        public float getAmount() {
            return amount;
        }

        public void setAmount(float amount) {
            this.amount = amount;
        }

        public int getCourseid() {
            return courseid;
        }

        public void setCourseid(int courseid) {
            this.courseid = courseid;
        }

        public String getTimeDate() {
            return timeDate;
        }

        public void setTimeDate(String timeDate) {
            this.timeDate = timeDate;
        }
    }

    // Nested class to represent Test Result details
    public static class TestResult {
        private int courseid;
        private int moduleNumber;
        private int totalMarks;
        private int userMarks;
        private String result; // e.g., Pass/Fail

        // Getters and Setters
        public int getCourseid() {
            return courseid;
        }

        public void setCourseid(int courseid) {
            this.courseid = courseid;
        }

        public int getModuleNumber() {
            return moduleNumber;
        }

        public void setModuleNumber(int moduleNumber) {
            this.moduleNumber = moduleNumber;
        }

        public int getTotalMarks() {
            return totalMarks;
        }

        public void setTotalMarks(int totalMarks) {
            this.totalMarks = totalMarks;
        }

        public int getUserMarks() {
            return userMarks;
        }

        public void setUserMarks(int userMarks) {
            this.userMarks = userMarks;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }
}
