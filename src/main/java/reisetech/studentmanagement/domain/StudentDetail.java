package reisetech.studentmanagement.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import reisetech.studentmanagement.date.Student;
import reisetech.studentmanagement.date.StudentCourse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetail {

  @NotNull(message = "受講生情報は必須です")
  @Valid
  private Student student;
  @Valid
  private List<StudentCourse> studentCourseList = new ArrayList<>();

}
