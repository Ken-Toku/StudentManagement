package reisetech.studentmanagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reisetech.studentmanagement.data.StudentCourseStatus;
import reisetech.studentmanagement.repository.StudentCourseStatusRepository;


@ExtendWith(MockitoExtension.class)
public class StudentCourseStatusServiceTest {

  @Mock
  private StudentCourseStatusRepository statusRepository;

  private StudentCourseStatusService sut;

  @BeforeEach
  void before() {
    sut = new StudentCourseStatusService(statusRepository);
  }

  @Test
  void 現在のステータスを取得_リポジトリが適切に呼び出されること及び戻り値の中身の検証() {

    StudentCourseStatus courseStatus = new StudentCourseStatus();
    courseStatus.setId(100);
    courseStatus.setStudentCourseId(1);
    courseStatus.setStatus("仮申込");

    Mockito.when(statusRepository.searchStudentCourseId(1)).thenReturn(courseStatus);

    StudentCourseStatus actual = sut.searchStudentCourseId(1);

    verify(statusRepository, times(1)).searchStudentCourseId(1);

    assertThat(actual.getId()).isEqualTo(100);
    assertThat(actual.getStudentCourseId()).isEqualTo(1);
    assertThat(actual.getStatus()).isEqualTo("仮申込");

  }

  @Test
  void ステータスを更新_既存のステータスを正しく更新できること() {
    StudentCourseStatus current = new StudentCourseStatus();
    current.setId(10);
    current.setStudentCourseId(1);
    current.setStatus("仮申込");

    Mockito.when(statusRepository.searchStudentCourseId(1)).thenReturn(current);

    sut.updateStatus(1, "本申込");

    verify(statusRepository, times(1)).searchStudentCourseId(1);

    ArgumentCaptor<StudentCourseStatus> captor = ArgumentCaptor.forClass(StudentCourseStatus.class);

    verify(statusRepository, times(1)).updateStudentCourseStatus(captor.capture());

    StudentCourseStatus update = captor.getValue();
    assertThat(update.getId()).isEqualTo(10);
    assertThat(update.getStudentCourseId()).isEqualTo(1);
    assertThat(update.getStatus()).isEqualTo("本申込");

  }

  @Test
  void ステータスを更新_存在しないIDを更新したときに例外が発生すること() {
    Mockito.when(statusRepository.searchStudentCourseId(1)).thenReturn(null);

    assertThatThrownBy(() -> sut.updateStatus(1, "本申込")).isInstanceOf(
        IllegalArgumentException.class);

    verify(statusRepository, times(1)).searchStudentCourseId(1);
    verify(statusRepository, never()).updateStudentCourseStatus(any(StudentCourseStatus.class));

  }

  @Test
  void ステータスを更新_存在しないステータスを更新したときに例外が発生すること() {
    StudentCourseStatus current = new StudentCourseStatus();
    current.setId(1);
    current.setStudentCourseId(1);
    current.setStatus("仮申込");

    Mockito.when(statusRepository.searchStudentCourseId(1)).thenReturn(current);

    assertThatThrownBy(() -> sut.updateStatus(1, "申込済（テスト）"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("ステータスの値が不正です:申込済（テスト）");

    verify(statusRepository, never()).updateStudentCourseStatus(any());
  }

  @Test
  void 受講生コースに初期ステータスを正しく登録できること() {
    sut.registerInitStatus(1);

    ArgumentCaptor<StudentCourseStatus> captor = ArgumentCaptor.forClass(StudentCourseStatus.class);

    verify(statusRepository, times(1)).registerStudentCourseStatus(captor.capture());

    StudentCourseStatus saved = captor.getValue();
    assertThat(saved.getStudentCourseId()).isEqualTo(1);
    assertThat(saved.getStatus()).isEqualTo("仮申込");
  }

}