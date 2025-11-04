package reisetech.studentmanagement.date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCourse {

  private Integer id;
  private Integer studentId;
  @NotBlank(message = "コース名は必須です")
  @Size(max = 20, message = "コース名は20文字以内で入力してください")
  private String courseName;
  private LocalDateTime enrollmentDate;
  private LocalDate completionDate;
  private String remark;
}
