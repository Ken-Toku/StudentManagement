package reisetech.studentmanagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import reisetech.studentmanagement.data.StudentCourseStatus;

@MybatisTest
class StudentCourseStatusRepositoryTest {

  @Autowired
  private StudentCourseStatusRepository sut;

  @Test
  void 受講生コース情報Idから申込状態を1件取得できること() {
    StudentCourseStatus actual = sut.searchStudentCourseId(1);

    assertThat(actual).isNotNull();
    assertThat(actual.getStudentCourseId()).isEqualTo(1);
    assertThat(actual.getStatus()).isEqualTo("仮申込");
  }

  @Test
  void 申込状態が登録されていない受講生Idではnullが返ること() {
    StudentCourseStatus actual = sut.searchStudentCourseId(5);

    assertThat(actual).isNull();
  }

  @Test
  void コースの申込状態を新規に登録できること() {
    StudentCourseStatus studentCourseStatus = new StudentCourseStatus();
    studentCourseStatus.setStudentCourseId(5);
    studentCourseStatus.setStatus("仮申込");

    sut.registerStudentCourseStatus(studentCourseStatus);

    StudentCourseStatus actual = sut.searchStudentCourseId(5);

    assertThat(actual.getId()).isNotNull();
    assertThat(actual.getStudentCourseId()).isEqualTo(5);
    assertThat(actual.getStatus()).isEqualTo("仮申込");
  }

  @Test
  void 既存の申込状態を更新できること() {
    StudentCourseStatus updateStatus = new StudentCourseStatus();
    updateStatus.setStudentCourseId(1);
    updateStatus.setStatus("受講終了");

    sut.updateStudentCourseStatus(updateStatus);
    StudentCourseStatus actual = sut.searchStudentCourseId(1);

    assertThat(actual).isNotNull();
    assertThat(actual.getStatus()).isEqualTo("受講終了");
  }
}