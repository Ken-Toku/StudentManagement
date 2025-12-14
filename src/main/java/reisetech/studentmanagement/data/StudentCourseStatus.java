package reisetech.studentmanagement.data;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * コースの申込状態を表すクラス student_course_status　DBテーブルに対応
 */

@Schema(description = "コースの申込状態")
@Data
public class StudentCourseStatus {

  @Schema(description = "コースステータスID")
  private Integer id;

  @Schema(description = "受講生コースID")
  @NotNull(message = "受講生コースIDは必須です")
  private Integer studentCourseId;

  @Schema(description = "ステータス（仮申込/本申込/受講中/受講終了）")
  @NotBlank(message = "ステータスは必須です")
  private String status;

}
