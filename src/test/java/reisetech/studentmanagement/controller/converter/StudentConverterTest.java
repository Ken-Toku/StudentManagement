package reisetech.studentmanagement.controller.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reisetech.studentmanagement.data.Student;
import reisetech.studentmanagement.data.StudentCourse;
import reisetech.studentmanagement.domain.StudentDetail;

class StudentConverterTest {

  private  StudentConverter sut;

  @BeforeEach
  void before(){
    sut = new StudentConverter();
  }

  @Test
  void 受講生IDに紐づくコースだけがマッピングされること() {
    Student student1 = new Student();
    student1.setId(1);
    student1.setName("山田太郎");

    Student student2 = new Student();
    student2.setId(2);
    student2.setName("佐藤花子");

    List<Student> studentList = List.of(student1, student2);

    StudentCourse course1 = new StudentCourse();
    course1.setStudentId(1);
    course1.setCourseName("Javaコース");

    StudentCourse course2 = new StudentCourse();
    course2.setStudentId(1);
    course2.setCourseName("AWSコース");

    StudentCourse course3 = new StudentCourse();
    course3.setStudentId(2);
    course3.setCourseName("デザインコース");

    List<StudentCourse> studentCourseList = List.of(course1, course2, course3);

    List<StudentDetail> result = sut.convertStudentDetailList(studentList, studentCourseList);

    assertThat(result).hasSize(2);

    StudentDetail detail = result.stream()
        .filter(d -> d.getStudent().getId().equals(1))
        .findFirst()
        .orElseThrow();

    assertThat(detail.getStudentCourseList())
        .extracting(StudentCourse::getCourseName)
        .containsExactlyInAnyOrder("Javaコース", "AWSコース");

    StudentDetail detail2 = result.stream()
        .filter(d -> d.getStudent().getId().equals(2))
        .findFirst()
        .orElseThrow();

    assertThat(detail2.getStudentCourseList())
        .extracting(StudentCourse::getCourseName)
        .containsExactly("デザインコース");
  }

  @Test
  void コースが存在しない受講生は空リストが設定されること() {
    Student student = new Student();
    student.setId(1);
    student.setName("山田太郎");
    List<Student> studentList = List.of(student);

    StudentCourse course = new StudentCourse();
    course.setStudentId(999);
    course.setCourseName("関係ないコース");
    List<StudentCourse> studentCourseList = List.of(course);

    List<StudentDetail> result = sut.convertStudentDetailList(studentList, studentCourseList);

    assertThat(result).hasSize(1);

    StudentDetail detail = result.get(0);
    assertThat(detail.getStudent().getId()).isEqualTo(1);

    assertThat(detail.getStudentCourseList()).isEmpty();

  }

  @Test
  void 受講生一覧が空なら空リストを返すこと(){
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();

    List<StudentDetail> result = sut.convertStudentDetailList(studentList, studentCourseList);

    assertThat(result).isEmpty();
  }

}