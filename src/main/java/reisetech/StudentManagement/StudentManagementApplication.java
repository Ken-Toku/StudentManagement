package reisetech.StudentManagement;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagementApplication {

		@Autowired
		private StudentRepository studentRepository;
		@Autowired
	  private StudentsCoursesRepository studentsCoursesRepository;

	public static void main(String[] args) {
		SpringApplication.run(StudentManagementApplication.class, args);
	}

	@GetMapping("/studentList")
	public List<Student> getstudentList() {
		return studentRepository.search();
	}

	@GetMapping("/studentsCourses")
	public List<StudentsCourses> getStudentCoursesList(){
		return studentsCoursesRepository.search();
	}
}