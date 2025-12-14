package reisetech.studentmanagement.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import reisetech.studentmanagement.data.Student;
import reisetech.studentmanagement.data.StudentCourse;
import reisetech.studentmanagement.domain.StudentDetail;
import reisetech.studentmanagement.exception.ResourceNotFoundException;
import reisetech.studentmanagement.service.StudentCourseStatusService;
import reisetech.studentmanagement.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private StudentService service;

  @MockitoBean
  private StudentCourseStatusService statusService;

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


  @Test
  void 受講生詳細の一覧検索が実行できて空のリストが返ってくること() throws Exception {

    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1)).searchStudentList();

  }

  @Test
  void サービスに受講生IDが渡されること() throws Exception {
    Integer id = 1;
    StudentDetail studentDetail = new StudentDetail();

    when(service.searchStudent(id)).thenReturn(studentDetail);

    mockMvc.perform(get("/student/{id}", id))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudent(id);
  }

  @Test
  void 受講生詳細の取得時に入力チェックが入ること() throws Exception {
    mockMvc.perform(get("/student/{id}", 0))
        .andExpect(status().isBadRequest());

    mockMvc.perform(get("/student/{id}", 1000))
        .andExpect(status().isBadRequest());

    verify(service, never()).searchStudent(anyInt());
  }

  @Test
  void 受講生詳細の登録が実行できて空で返ってくること() throws Exception {
    mockMvc.perform(post("/registerStudent").contentType(MediaType.APPLICATION_JSON).content(
            """
                {
                "student": {
                        "name": "小林彩",
                        "furigana": "こばやしあや",
                        "age": 32,
                        "gender": "女性",
                        "nickname": "あーちゃん",
                        "email": "atyan@test.com",
                        "city": "東京都",
                        "remark": ""
                        },
                    "studentCourseList": [
                        {
                            "courseName": "マーケティングコース"
                        }
                    ]
                }
                """
        ))
        .andExpect(status().isOk());

    verify(service, times(1)).registerStudent(any());
  }

  @Test
  void 受講生詳細の更新が実行できて空で返ってくること() throws Exception {
    mockMvc.perform(put("/updateStudent").contentType(MediaType.APPLICATION_JSON).content(
            """
                {
                "student": {
                        "id":11,
                        "name": "小林彩",
                        "furigana": "こばやしあや",
                        "age": 32,
                        "gender": "女性",
                        "nickname": "あーちゃん",
                        "email": "atyan@test.com",
                        "city": "東京都",
                        "remark": "",
                        "deleted": false
                        },
                    "studentCourseList": [
                        {
                            "id": 7,
                            "studentId": 11,
                            "courseName": "マーケティングコース",
                            "enrollmentDate": "2025-10-26T03:11:35",
                            "completionDate": "2026-10-26",
                            "remark": null
                        }
                    ]
                }
                """
        ))
        .andExpect(status().isOk());

    verify(service, times(1)).updateStudent(any());
  }

  @Test
  void 受講生詳細の受講生で適切な値を入力したときに入力チェックに異常が発生しないこと() {
    Student student = new Student();
    student.setName("佐藤　太郎");
    student.setFurigana("さとう　たろう");
    student.setAge(30);
    student.setGender("男性");
    student.setEmail("test@example.com");
    student.setCity("東京都");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 受講生詳細の受講生で名前が空白だったときに入力チェックが掛かること() {
    Student student = new Student();
    student.setName("");
    student.setFurigana("さとう　たろう");
    student.setAge(30);
    student.setGender("男性");
    student.setEmail("test@example.com");
    student.setCity("東京都");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message").containsOnly("名前は必須です");

  }

  @Test
  void 受講生詳細の一覧検索が実行できてJSON構造が正しいこと() throws Exception {

    Student student = new Student();
    student.setId(1);
    student.setName("山田太郎");
    student.setFurigana("やまだたろう");
    student.setAge(30);
    student.setGender("男性");
    student.setNickname("たろう");
    student.setEmail("taro@example.com");
    student.setCity("東京都");
    student.setRemark("メモ");

    StudentCourse course = new StudentCourse();
    course.setId(10);
    course.setStudentId(1);
    course.setCourseName("Javaコース");
    course.setStatus("仮申込");

    StudentDetail detail = new StudentDetail();
    detail.setStudent(student);
    detail.setStudentCourseList(List.of(course));

    when(service.searchStudentList()).thenReturn(List.of(detail));

    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk())

        .andExpect(jsonPath("$.length()").value(1))

        .andExpect(jsonPath("$[0].student.id").value(1))
        .andExpect(jsonPath("$[0].student.name").value("山田太郎"))
        .andExpect(jsonPath("$[0].student.furigana").value("やまだたろう"))
        .andExpect(jsonPath("$[0].student.age").value(30))
        .andExpect(jsonPath("$[0].student.gender").value("男性"))
        .andExpect(jsonPath("$[0].student.nickname").value("たろう"))
        .andExpect(jsonPath("$[0].student.email").value("taro@example.com"))
        .andExpect(jsonPath("$[0].student.city").value("東京都"))
        .andExpect(jsonPath("$[0].student.remark").value("メモ"))

        .andExpect(jsonPath("$[0].studentCourseList.length()").value(1))
        .andExpect(jsonPath("$[0].studentCourseList[0].id").value(10))
        .andExpect(jsonPath("$[0].studentCourseList[0].studentId").value(1))
        .andExpect(jsonPath("$[0].studentCourseList[0].courseName").value("Javaコース"))
        .andExpect(jsonPath("$[0].studentCourseList[0].status").value("仮申込"));

    verify(service, times(1)).searchStudentList();
  }

  @Test
  void コースステータスの更新が実行できてメッセージが返ってくること() throws Exception {

    mockMvc.perform(put("/updateCourseStatus")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                 "studentCourseId":1,
                 "status":"本申込"
                }
                """))
        .andExpect(status().isOk())
        .andExpect(content().string("ステータスの更新処理が成功しました。"));

    verify(statusService, times(1)).updateStatus(1, "本申込");
  }

  @Test
  void コースステータスの更新でステータスが空文字の場合はバリデーションエラーとなり400が返ること()
      throws Exception {

    String json = """
        {
         "studentCourseId": 1,
         "status": ""
        }
        """;

    mockMvc.perform(put("/updateCourseStatus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest());

    verify(statusService, never()).updateStatus(anyInt(), any());
  }

  @Test
  void コースステータスの更新で受講生コースIDが欠落している場合はバリデーションエラーとなり400が返ること()
      throws Exception {

    String json = """
        {
          "status": "本申込"
        }
        """;

    mockMvc.perform(put("/updateCourseStatus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest());

    verify(statusService, never()).updateStatus(anyInt(), any());

  }

  @Test
  void コースステータスの更新で不正なステータスの入力は400が返ること() throws Exception {

    doThrow(new IllegalArgumentException("ステータスの値が不正です：キャンセル"))
        .when(statusService).updateStatus(1, "キャンセル");

    String json = """
        {
         "studentCourseId": 1,
         "status": "キャンセル"
        }
        """;

    mockMvc.perform(put("/updateCourseStatus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest());

    verify(statusService, times(1)).updateStatus(1, "キャンセル");
  }

  @Test
  void コースステータスの更新で存在しないコースIDの場合は404が返ること() throws Exception {
    doThrow(new ResourceNotFoundException("指定されたコースIDのステータスが存在しません：999"))
        .when(statusService).updateStatus(999, "本申込");

    String json = """
        {
         "studentCourseId": 999,
         "status": "本申込"
        }
        """;

    mockMvc.perform(put("/updateCourseStatus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isNotFound());

    verify(statusService, times(1)).updateStatus(999, "本申込");
  }
}