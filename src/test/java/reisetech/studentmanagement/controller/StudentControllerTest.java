package reisetech.studentmanagement.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester.MockMvcRequestBuilder;
import reisetech.studentmanagement.date.Student;
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

    mockMvc.perform(get("/studentList")).andExpect(status().isOk());

    verify(service, times(1)).searchStudentList();

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
    assertThat(violations).extracting("message")
        .containsOnly("名前は必須です");

  }

}