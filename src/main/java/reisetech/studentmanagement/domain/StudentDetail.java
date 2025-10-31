package reisetech.studentmanagement.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import reisetech.studentmanagement.date.Student;
import reisetech.studentmanagement.date.StudentCourse;

@Getter
@Setter
public class StudentDetail {

  private Student student;
  private List<StudentCourse> studentCourses = new ArrayList<>();

}
