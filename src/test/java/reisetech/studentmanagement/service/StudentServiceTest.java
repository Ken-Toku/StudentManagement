package reisetech.studentmanagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reisetech.studentmanagement.controller.converter.StudentConverter;
import reisetech.studentmanagement.date.Student;
import reisetech.studentmanagement.date.StudentCourse;
import reisetech.studentmanagement.domain.StudentDetail;
import reisetech.studentmanagement.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter);
  }


  @Test
  void 登録済みの受講生情報を全件取得_リポジトリとコンバーターの処理が適切に呼び出されていること() {
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();

    Mockito.when(repository.search()).thenReturn(studentList);
    Mockito.when(repository.searchStudentCourseList()).thenReturn(studentCourseList);

    List<StudentDetail> actual = sut.searchStudentList();

    verify(repository, times(1)).search();
    verify(repository, times(1)).searchStudentCourseList();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);
  }


  @Test
  void 受講生IDを指定して検索_受講生とコース情報をまとめてStudentDetailとして取得できること() {
    Integer id = 1;

    Student student = new Student();
    student.setId(id);

    List<StudentCourse> studentCourseList = new ArrayList<>();
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId(id);
    studentCourseList.add(studentCourse);

    Mockito.when(repository.searchStudent(id)).thenReturn(student);
    Mockito.when(repository.searchStudentCourse(id)).thenReturn(studentCourseList);

    StudentDetail actual = sut.searchStudent(id);

    verify(repository, times(1)).searchStudent(id);
    verify(repository, times(1)).searchStudentCourse(id);

    assertSame(student, actual.getStudent());
    assertEquals(studentCourseList, actual.getStudentCourseList());
  }


  @Test
  void 受講生と受講生コース情報の登録_受講生と受講生コース情報登録処理が呼び出されること() {
    Integer id = 1;

    Student student = new Student();
    student.setId(id);

    StudentCourse course1 = new StudentCourse();
    StudentCourse course2 = new StudentCourse();

    List<StudentCourse> studentCourseList = new ArrayList<>();
    studentCourseList.add(course1);
    studentCourseList.add(course2);

    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    StudentDetail actual = sut.registerStudent(studentDetail);

    verify(repository, times(1)).registerStudent(student);
    verify(repository, times(1)).registerStudentCourse(course1);
    verify(repository, times(1)).registerStudentCourse(course2);

    assertSame(studentDetail, actual);
  }

  @Test
  void 受講生コース情報初期化_受講生IDと修了予定日が正しく設定されること() {
    Student student = new Student();
    student.setId(1);

    StudentCourse studentCourse = new StudentCourse();

    LocalDate expectedCompletionDate = LocalDate.now().plusYears(1);
    StudentService.initStudentCourse(studentCourse, student);

    assertEquals(1, studentCourse.getStudentId());
    assertEquals(expectedCompletionDate, studentCourse.getCompletionDate());
  }

  @Test
  void 受講生情報更新_受講生とコース情報の更新処理が呼び出されコースに受講生IDが設定されること() {
    Integer id = 1;
    Student student = new Student();
    student.setId(id);

    StudentCourse course1 = new StudentCourse();
    StudentCourse course2 = new StudentCourse();

    List<StudentCourse> studentCourseList = new ArrayList<>();
    studentCourseList.add(course1);
    studentCourseList.add(course2);

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentCourseList(studentCourseList);

    sut.updateStudent(studentDetail);

    verify(repository, times(1)).updateStudentCourse(course1);
    verify(repository, times(1)).updateStudentCourse(course2);

    assertEquals(id, course1.getStudentId());
    assertEquals(id, course2.getStudentId());
  }
}