package reisetech.studentmanagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import reisetech.studentmanagement.date.Student;
import reisetech.studentmanagement.date.StudentCourse;

/**
 * 受講生情報を扱うリポジトリ。 全件検索や単一条件での検索、コース情報の検索が行えるクラスです。
 */

@Mapper
public interface StudentRepository {

  /**
   * @return 全件検索した受講生情報の一覧
   */

  @Select("SELECT * FROM students ")
  List<Student> search();

  @Select("SELECT * FROM students WHERE id = #{id}")
  Student searchStudent(Integer id);

  @Select("SELECT * FROM students_courses")
  List<StudentCourse> searchStudentsCourses();

  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<StudentCourse> searchStudentCourse(Integer studentId);


  //　新規受講生登録
  @Insert("INSERT INTO students(name, furigana, age, gender, nickname, email, city, remark)"
      + "VALUES(#{name}, #{furigana}, #{age}, #{gender}, #{nickname}, #{email}, #{city}, #{remark})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudent(Student student);

  //　新規コース登録
  @Insert(
      "INSERT INTO students_courses(student_id, course_name, completion_date, remark)"
          + "VALUES(#{studentId}, #{courseName}, #{completionDate}, #{remark} ")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudentsCourses(StudentCourse studentCourse);

  //　受講生情報更新
  @Update(
      "UPDATE students SET name = #{name}, furigana = #{furigana}, age = #{age}, gender = #{gender}, nickname = #{nickname}, "
          + "email = #{email}, city = #{city}, remark = #{remark}, isDeleted = #{deleted} WHERE id = #{id}")
  void updateStudent(Student student);

  //　コース情報更新
  @Update(
      "UPDATE students_courses SET course_name = #{courseName} WHERE id = #{id}")
  void updateStudentsCourses(StudentCourse studentCourse);


}