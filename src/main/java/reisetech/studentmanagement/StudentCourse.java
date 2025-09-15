package reisetech.studentmanagement;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCourse {

  private int id;
  private int studentId;
  private String courseName;
  private LocalDateTime enrollmentDate;
  private LocalDate completionDate;
}
