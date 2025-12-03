package reisetech.studentmanagement.data;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * コースの申込状態を表すクラス
 * student_course_status　DBテーブルに対応
 */

@Schema(description = "コースの申込状態")
@Data
public class StudentCourseStatus {
private Integer id;
private Integer studentCourseId;
private String status;

}
