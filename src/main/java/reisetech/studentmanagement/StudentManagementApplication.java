package reisetech.studentmanagement;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagementApplication {

  @Autowired
  private StudentRepository studentRepository;
  @Autowired
  private StudentCourseRepository studentCourseRepository;

  public static void main(String[] args) {
    SpringApplication.run(StudentManagementApplication.class, args);
  }

  @GetMapping("/studentList")
  public List<Student> getStudentList() {
    return studentRepository.search();
  }

  @GetMapping("/studentsCourses")
  public List<StudentCourse> getStudentCourseList() {
    return studentCourseRepository.search();
  }
}