package reisetech.studentmanagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
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
import reisetech.studentmanagement.data.Student;
import reisetech.studentmanagement.data.StudentCourse;
import reisetech.studentmanagement.data.StudentCourseStatus;
import reisetech.studentmanagement.domain.StudentDetail;
import reisetech.studentmanagement.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  @Mock
  private StudentCourseStatusService courseStatusService;

  private StudentService sut;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter, courseStatusService);
  }


  @Test
  void 登録済みの受講生情報を全件取得_リポジトリとコンバーターの処理が適切に呼び出されていることおよび戻り値の中身の検証() {

    List<Student> studentList = new ArrayList<>();
    Student student = new Student();
    student.setId(1);
    student.setName("山田太郎");
    studentList.add(student);

    List<StudentCourse> studentCourseList = new ArrayList<>();
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId(1);
    studentCourse.setCourseName("Java");
    studentCourseList.add(studentCourse);

    List<StudentDetail> studentDetailList = new ArrayList<>();
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);
    studentDetailList.add(studentDetail);

    Mockito.when(repository.search()).thenReturn(studentList);
    Mockito.when(repository.searchStudentCourseList()).thenReturn(studentCourseList);
    Mockito.when((converter.convertStudentDetailList(studentList, studentCourseList)))
        .thenReturn(studentDetailList);

    List<StudentDetail> actual = sut.searchStudentList();

    verify(repository, times(1)).search();
    verify(repository, times(1)).searchStudentCourseList();
    verify(converter, times(1)).convertStudentDetailList(studentList, studentCourseList);

    assertThat(actual)
        .hasSize(1);
    assertThat(actual.getFirst())
        .isSameAs(studentDetail);
    assertThat(actual.getFirst().getStudent())
        .isSameAs(student);
    assertThat(actual.getFirst().getStudentCourseList())
        .hasSize(1);
    assertThat(actual.getFirst().getStudentCourseList().getFirst())
        .isSameAs(studentCourse);
  }

  @Test
  void 登録済みの受講生情報を全件取得_各コース情報にステータスが設定されていること() {
    List<Student> studentList = new ArrayList<>();
    Student student = new Student();
    student.setId(1);
    student.setName("山田太郎");
    studentList.add(student);

    List<StudentCourse> studentCourseList = new ArrayList<>();
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId(1);
    studentCourse.setStudentId(1);
    studentCourse.setCourseName("Javaコース");
    studentCourseList.add(studentCourse);

    StudentCourseStatus courseStatus = new StudentCourseStatus();
    courseStatus.setId(100);
    courseStatus.setStudentCourseId(1);
    courseStatus.setStatus("受講中");

    Mockito.when(repository.search()).thenReturn(studentList);
    Mockito.when(repository.searchStudentCourseList()).thenReturn(studentCourseList);
    Mockito.when(courseStatusService.searchStudentCourseId(1)).thenReturn(courseStatus);

    List<StudentDetail> studentDetailList = new ArrayList<>();
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);
    studentDetailList.add(studentDetail);
    Mockito.when(converter.convertStudentDetailList(studentList, studentCourseList))
        .thenReturn(studentDetailList);

    List<StudentDetail> actual = sut.searchStudentList();

    verify(courseStatusService, times(1)).searchStudentCourseId(1);

    assertThat(actual).hasSize(1);
    assertThat(actual.getFirst().getStudentCourseList()).hasSize(1);
    assertThat(actual.getFirst().getStudentCourseList().getFirst().getStatus()).isEqualTo("受講中");

  }


  @Test
  void 登録済みの受講生情報を全件取得_リポジトリが例外を投げた場合その例外が呼び出し元に伝達されること() {
    Mockito.when(repository.search()).thenThrow(new RuntimeException("DB error"));

    assertThrows(RuntimeException.class, () -> sut.searchStudentList());

    verify(repository, never()).searchStudentCourseList();
    verify(converter, never()).convertStudentDetailList(anyList(), anyList());

  }

  @Test
  void 受講生IDに紐づく受講生情報を取得＿存在しないIDの場合はNullPointerExceptionが発生すること() {
    Integer notId = 999;

    Mockito.when(repository.searchStudent(notId)).thenReturn(null);

    assertThrows(NullPointerException.class, () -> sut.searchStudent(notId));

    verify(repository, never()).searchStudentCourse(anyInt());
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

    assertThat(actual.getStudent())
        .isSameAs(student);
    assertThat(actual.getStudentCourseList())
        .isEqualTo(studentCourseList);
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

    assertThat(actual).isSameAs(studentDetail);
  }

  @Test
  void 受講生と受講生コース情報の登録＿受講生情報登録処理実施時に例外が発生した場合コース登録処理が実施されないこと() {

    Student student = new Student();
    student.setId(1);

    StudentCourse studentCourse1 = new StudentCourse();
    StudentCourse studentCourse2 = new StudentCourse();

    List<StudentCourse> studentCourseList = new ArrayList<>();
    studentCourseList.add(studentCourse1);
    studentCourseList.add(studentCourse2);

    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    Mockito.doThrow(new RuntimeException("DB error")).when(repository).registerStudent(student);

    assertThrows(RuntimeException.class, () -> sut.registerStudent(studentDetail));

    verify(repository, never()).registerStudentCourse(any(StudentCourse.class));

  }


  @Test
  void 受講生コース情報初期化_受講生IDと修了予定日が正しく設定されること() {
    Student student = new Student();
    student.setId(1);

    StudentCourse studentCourse = new StudentCourse();

    LocalDate expectedCompletionDate = LocalDate.now().plusYears(1);
    StudentService.initStudentCourse(studentCourse, student.getId());

    assertThat(studentCourse.getStudentId())
        .isEqualTo(1);
    assertThat(studentCourse.getCompletionDate())
        .isEqualTo(expectedCompletionDate);
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

    assertThat(course1.getStudentId())
        .isEqualTo(id);
    assertThat(course2.getStudentId())
        .isEqualTo(id);
  }
}