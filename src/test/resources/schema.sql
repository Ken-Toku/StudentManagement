CREATE TABLE IF NOT EXISTS students (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100),
  furigana VARCHAR(100),
  age INT,
  gender VARCHAR(20),
  nickname VARCHAR(50),
  email VARCHAR(255),
  city VARCHAR(50),
  remark VARCHAR(255),
  isDeleted INT
);

CREATE TABLE IF NOT EXISTS students_courses (
  id INT AUTO_INCREMENT PRIMARY KEY,
  student_id INT,
  course_name VARCHAR(100),
  enrollment_date TIMESTAMP,
  completion_date DATE,
  remark VARCHAR(255),
  isDeleted INT
);
