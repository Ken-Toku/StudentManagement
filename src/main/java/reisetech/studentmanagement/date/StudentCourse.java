package reisetech.studentmanagement.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCourse {

  private Integer id;
  private Integer studentId;
  private String courseName;
  private LocalDateTime enrollmentDate;
  private LocalDate completionDate;
  private String remark;
}
