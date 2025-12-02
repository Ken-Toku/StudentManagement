package reisetech.studentmanagement.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import reisetech.studentmanagement.data.Student;
import reisetech.studentmanagement.data.StudentCourse;

@Schema(description = "受講生詳細")
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
