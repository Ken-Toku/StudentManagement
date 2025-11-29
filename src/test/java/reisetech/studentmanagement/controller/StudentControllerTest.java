package reisetech.studentmanagement.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import reisetech.studentmanagement.date.Student;
import reisetech.studentmanagement.domain.StudentDetail;
import reisetech.studentmanagement.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private StudentService service;

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
  void NotFoundExceptionがハンドリングされエラーが返ること() throws Exception {
    mockMvc.perform(get("/exception"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("このAPIは現在利用できません。古いURLとなっています。"));
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




}