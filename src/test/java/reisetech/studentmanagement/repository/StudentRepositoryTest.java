package reisetech.studentmanagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import reisetech.studentmanagement.data.Student;
import reisetech.studentmanagement.data.StudentCourse;


@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生の全件検索が行えること() {
    List<Student> actual = sut.search();
    assertThat(actual.size()).isEqualTo(5);
  }

  @Test
  void 受講生の単一検索が行えること() {

    Student actual = sut.searchStudent(1);

    assertThat(actual).isNotNull();

    assertThat(actual.getId()).isEqualTo(1);
    assertThat(actual.getName()).isEqualTo("山田 正志");
  }

  @Test
  void 受講生コース情報の全件検索が行えること() {
    List<StudentCourse> actual = sut.searchStudentCourseList();
    assertThat(actual.size()).isEqualTo(6);
  }

  @Test
  void 受講生IDに紐づく受講生コース情報を検索できること() {
    List<StudentCourse> actual = sut.searchStudentCourse(3);

    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual.getFirst().getStudentId()).isEqualTo(3);

  }

  @Test
  void 受講生の登録が行えること() {
    Student student = new Student();

    student.setName("山田太郎");
    student.setFurigana("やまだたろう");
    student.setAge(35);
    student.setNickname("だーやま");
    student.setEmail("test@example.com");
    student.setCity("北海道");
    student.setRemark("");
    student.setDeleted(false);

    sut.registerStudent(student);

    List<Student> actual = sut.search();

    assertThat(actual.size()).isEqualTo(6);

  }

  @Test
  void 受講生コース情報を新規登録できること() {
    StudentCourse course = new StudentCourse();
    course.setStudentId(1);
    course.setCourseName("テストコース");

    sut.registerStudentCourse(course);

    List<StudentCourse> actual = sut.searchStudentCourseList();

    assertThat(actual.size()).isEqualTo(7);


  }

  @Test
  void 受講生情報を更新できること() {
    Student student = sut.searchStudent(1);

    student.setAge(99);
    sut.updateStudent(student);
    Student acutual = sut.searchStudent(1);

    assertThat(acutual.getAge()).isEqualTo(99);
  }

  @Test
  void 受講生コース情報のコース名を更新できること(){
    List<StudentCourse> courseList = sut.searchStudentCourseList();
    StudentCourse studentCourse = courseList.getFirst();

    studentCourse.setCourseName("テストコース");
    sut.updateStudentCourse(studentCourse);

    List<StudentCourse> actualCourseList = sut.searchStudentCourseList();
    StudentCourse actualStudentCourse = actualCourseList.getFirst();

    assertThat(actualStudentCourse.getCourseName()).isEqualTo("テストコース");
  }

}