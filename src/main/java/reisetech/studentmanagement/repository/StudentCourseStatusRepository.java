package reisetech.studentmanagement.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import reisetech.studentmanagement.data.StudentCourseStatus;


/**
 * コース申込状態テーブルと紐づくRepositoryです。
 */
@Mapper
public interface StudentCourseStatusRepository {

  /**
   * 受講生コース情報Idから申込状態を１件取得します。
   *
   * @param studentCourseId 受講生コース情報のID
   * @return 存在する場合は StudentCourseStatus。存在しない場合は null。
   */
  @Select("""
      SELECT
        id,
        student_course_id,
        status
      FROM student_course_status
      WHERE student_course_id = #{studentCourseId}
      """)
  StudentCourseStatus searchStudentCourseId(Integer studentCourseId);

  /**
   * コースの申込状態を新規に登録します。
   */
  @Insert("""
      INSERT INTO student_course_status(
        student_course_id,
        status
      )
      VALUES(
        #{studentCourseId},
        #{status}
      )
      """)
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudentCourseStatus(StudentCourseStatus studentCourseStatus);

  /**
   * 既存のコース申込状態を更新します。
   */
  @Update("""
      UPDATE student_course_status
      SET status = #{status}
      WHERE student_course_id = #{studentCourseId}
      """)
  void updateStudentCourseStatus(StudentCourseStatus studentCourseStatus);


}
