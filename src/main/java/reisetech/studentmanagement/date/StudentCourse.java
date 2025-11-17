package reisetech.studentmanagement.date;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生コース情報")
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
