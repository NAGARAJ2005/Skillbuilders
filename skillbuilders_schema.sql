
-- Create database
CREATE DATABASE skillbuilders;

-- Create new user and grant privileges
CREATE USER 'skillbuilder'@'localhost' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON skillbuilders.* TO 'skillbuilder'@'localhost';

-- Switch to the new database
USE skillbuilders;

-- Create users table
CREATE TABLE users (
    userid INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(25) NOT NULL,
    gender VARCHAR(10),
    email VARCHAR(50) NOT NULL UNIQUE,
    phone_number BIGINT,
    password VARCHAR(100) NOT NULL,
    grade VARCHAR(25),
    stream VARCHAR(25),
    country VARCHAR(25),
    city VARCHAR(25),
    professional_summary VARCHAR(500),
    DOB DATE,
    profile VARCHAR(50)
);
ALTER TABLE users AUTO_INCREMENT = 100000;

-- Create instructors table
CREATE TABLE instructors (
    instructorid INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(25) NOT NULL,
    background VARCHAR(30) NOT NULL,
    rating FLOAT DEFAULT 0,
    rating_count INT DEFAULT 0,
    review_count INT DEFAULT 0,
    course_count INT DEFAULT 0,
    profile VARCHAR(50) NOT NULL
);
ALTER TABLE instructors AUTO_INCREMENT = 200000;

-- Create streams table
CREATE TABLE streams (
    stream VARCHAR(25) PRIMARY KEY
);

-- Create testcourses table
CREATE TABLE testcourses (
    courseid INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    instructorid INT NOT NULL,
    price FLOAT NOT NULL,
    duration FLOAT NOT NULL,
    module_count INT NOT NULL,
    thumbnail VARCHAR(50) NOT NULL,
    description VARCHAR(300) NOT NULL,
    CONSTRAINT fk_instructorid_testcourses FOREIGN KEY (instructorid)
        REFERENCES instructors(instructorid) ON DELETE CASCADE
);
ALTER TABLE testcourses AUTO_INCREMENT = 700000;

-- Create courses table
CREATE TABLE courses (
    courseid INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    instructorid INT NOT NULL,
    price FLOAT NOT NULL,
    rating FLOAT DEFAULT 0,
    rating_count INT DEFAULT 0,
    duration FLOAT NOT NULL,
    module_count INT NOT NULL,
    enrolled_count INT DEFAULT 0,
    time_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    thumbnail VARCHAR(50) NOT NULL,
    description VARCHAR(300) NOT NULL,
    CONSTRAINT fk_instructorid FOREIGN KEY (instructorid)
        REFERENCES instructors(instructorid) ON DELETE CASCADE
);
ALTER TABLE courses AUTO_INCREMENT = 300000;

-- Create usercourses table
CREATE TABLE usercourses (
    userid INT,
    courseid INT,
    course_type VARCHAR(35) NOT NULL,
    progress INT DEFAULT 0,
    PRIMARY KEY (userid, courseid),
    FOREIGN KEY (userid) REFERENCES users(userid),
    FOREIGN KEY (courseid) REFERENCES courses(courseid)
);

-- Create userInterestedStream table
CREATE TABLE userInterestedStream (
    userid INT NOT NULL,
    stream VARCHAR(25) NOT NULL,
    PRIMARY KEY (userid, stream),
    FOREIGN KEY (userid) REFERENCES users(userid) ON DELETE CASCADE,
    FOREIGN KEY (stream) REFERENCES streams(stream) ON DELETE CASCADE
);

-- Create courseStreams table
CREATE TABLE courseStreams (
    courseid INT NOT NULL,
    stream VARCHAR(25) NOT NULL,
    PRIMARY KEY (courseid, stream),
    FOREIGN KEY (courseid) REFERENCES courses(courseid) ON DELETE CASCADE,
    FOREIGN KEY (stream) REFERENCES streams(stream) ON DELETE CASCADE
);

-- Create coursePrerequisites table
CREATE TABLE coursePrerequisites (
    courseid INT NOT NULL,
    prerequisite VARCHAR(100) NOT NULL,
    PRIMARY KEY (courseid, prerequisite),
    FOREIGN KEY (courseid) REFERENCES courses(courseid) ON DELETE CASCADE
);

-- Create lectures table
CREATE TABLE lectures (
    courseid INT NOT NULL,
    module_number INT NOT NULL,
    module_name VARCHAR(100) NOT NULL,
    link VARCHAR(50) NOT NULL,
    PRIMARY KEY (courseid, module_number),
    FOREIGN KEY (courseid) REFERENCES courses(courseid) ON DELETE CASCADE
);

-- Create questions table
CREATE TABLE questions (
    question_number INT NOT NULL,
    question VARCHAR(100) NOT NULL,
    option1 VARCHAR(100) NOT NULL,
    option2 VARCHAR(100) NOT NULL,
    option3 VARCHAR(100) NOT NULL,
    option4 VARCHAR(100) NOT NULL,
    answer VARCHAR(100) NOT NULL,
    courseid INT NOT NULL,
    module_number INT NOT NULL,
    PRIMARY KEY (courseid, module_number, question_number),
    FOREIGN KEY (courseid) REFERENCES courses(courseid) ON DELETE CASCADE
);

-- Create testResult table
CREATE TABLE testResult (
    courseid INT NOT NULL,
    userid INT NOT NULL,
    module_number INT NOT NULL,
    total_marks INT NOT NULL,
    user_marks INT NOT NULL,
    result VARCHAR(10) NOT NULL,
    PRIMARY KEY (courseid, userid, module_number),
    FOREIGN KEY (courseid) REFERENCES courses(courseid) ON DELETE CASCADE,
    FOREIGN KEY (userid) REFERENCES users(userid) ON DELETE CASCADE
);

-- Create certificates table
CREATE TABLE certificates (
    userid INT NOT NULL,
    courseid INT NOT NULL,
    path VARCHAR(50) NOT NULL,
    PRIMARY KEY (userid, courseid),
    FOREIGN KEY (userid) REFERENCES users(userid) ON DELETE CASCADE,
    FOREIGN KEY (courseid) REFERENCES courses(courseid) ON DELETE CASCADE
);

-- Create reviews table
CREATE TABLE reviews (
    reviewid INT AUTO_INCREMENT PRIMARY KEY,
    userid INT NOT NULL,
    courseid INT NOT NULL,
    rating INT NOT NULL,
    review VARCHAR(300) NOT NULL,
    date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (userid) REFERENCES users(userid) ON DELETE CASCADE,
    FOREIGN KEY (courseid) REFERENCES courses(courseid) ON DELETE CASCADE
);
ALTER TABLE reviews AUTO_INCREMENT = 500000;

-- Create transactions table
CREATE TABLE transactions (
    transactionid INT AUTO_INCREMENT PRIMARY KEY,
    amount FLOAT NOT NULL,
    courseid INT NOT NULL,
    userid INT NOT NULL,
    time_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (courseid) REFERENCES courses(courseid) ON DELETE CASCADE,
    FOREIGN KEY (userid) REFERENCES users(userid) ON DELETE CASCADE
);
ALTER TABLE transactions AUTO_INCREMENT = 400000;

-- Create courses_test table
CREATE TABLE courses_test (
    courseid INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    instructorid INT NOT NULL,
    price FLOAT NOT NULL,
    rating FLOAT DEFAULT 0,
    rating_count INT DEFAULT 0,
    duration FLOAT NOT NULL,
    module_count INT NOT NULL,
    enrolled_count INT DEFAULT 0,
    time_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    thumbnail VARCHAR(50) NOT NULL,
    description VARCHAR(300) NOT NULL
);

-- Create questions_test table
CREATE TABLE questions_test (
    question_number INT NOT NULL,
    question VARCHAR(100) NOT NULL,
    option1 VARCHAR(100) NOT NULL,
    option2 VARCHAR(100) NOT NULL,
    option3 VARCHAR(100) NOT NULL,
    option4 VARCHAR(100) NOT NULL,
    answer VARCHAR(100) NOT NULL,
    courseid INT NOT NULL,
    module_number INT NOT NULL,
    PRIMARY KEY (courseid, module_number, question_number),
    CONSTRAINT fk_courseid_questions_test FOREIGN KEY (courseid)
        REFERENCES courses(courseid) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Create coursestreams_test table
CREATE TABLE coursestreams_test (
    courseid INT NOT NULL,
    stream VARCHAR(25) NOT NULL,
    PRIMARY KEY (courseid, stream),
    CONSTRAINT fk_courseid_test FOREIGN KEY (courseid) REFERENCES courses(courseid) ON DELETE CASCADE,
    CONSTRAINT fk_stream_course_test FOREIGN KEY (stream) REFERENCES streams(stream) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Create lectures_test table
CREATE TABLE lectures_test (
    courseid INT NOT NULL,
    module_number INT NOT NULL,
    module_name VARCHAR(100) NOT NULL,
    link VARCHAR(50) NOT NULL,
    PRIMARY KEY (courseid, module_number),
    CONSTRAINT fk_courseid_lectures_test FOREIGN KEY (courseid) REFERENCES courses(courseid) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
