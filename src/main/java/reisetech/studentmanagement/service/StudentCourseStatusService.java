package reisetech.studentmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reisetech.studentmanagement.data.StudentCourseStatus;
import reisetech.studentmanagement.repository.StudentCourseStatusRepository;

@Service
public class StudentCourseStatusService {

  private static final String STATUS_1 = "仮申込";
  private static final String STATUS_2 = "本申込";
  private static final String STATUS_3 = "受講中";
  private static final String STATUS_4 = "受講終了";

  private final StudentCourseStatusRepository repository;

  @Autowired
  public StudentCourseStatusService(StudentCourseStatusRepository repository){
    this.repository = repository;
  }

  /**
   * 現在のステータスを取得する
   */
  public StudentCourseStatus searchStudentCourseId(Integer studentCourseId) {
    return repository.searchStudentCourseId(studentCourseId);
  }

  /**
   * ステータスを更新する
   */
  @Transactional
  public void updateStatus(Integer studentCourseId, String newStatus) {
    StudentCourseStatus current = repository.searchStudentCourseId(studentCourseId);
    if (current == null) {
      throw new IllegalArgumentException(
          "指定されたコースIDのステータスが存在しません" + studentCourseId);
    }

    current.setStatus(newStatus);
    repository.updateStudentCourseStatus(current);
  }

  public void updateToKarimoushikomi(Integer studentCourseId) {
    updateStatus(studentCourseId, STATUS_1);
  }

  public void updateToHonmoushikomi(Integer studentCourseId) {
    updateStatus(studentCourseId, STATUS_2);
  }

  public void updateToTaking(Integer studentCourseId) {
    updateStatus(studentCourseId, STATUS_3);
  }

  public void updateToFinished(Integer studentCourseId) {
    updateStatus(studentCourseId, STATUS_4);
  }

  /**
   * 受講生コースに初期ステータスを登録する。
   */
  @Transactional
  public void registerInitStatus(Integer studentCourseId) {
    StudentCourseStatus status = new StudentCourseStatus();
    status.setStudentCourseId(studentCourseId);
    status.setStatus(STATUS_1);

    repository.registerStudentCourseStatus(status);
  }

}
