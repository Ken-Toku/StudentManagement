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

CREATE TABLE IF NOT EXISTS student_course_status (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_course_id INT NOT NULL,
    status VARCHAR(20) NOT NULL COMMENT '仮申込 / 本申込 / 受講中 / 受講終了',
    UNIQUE (student_course_id)
);