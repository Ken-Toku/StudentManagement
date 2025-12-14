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

  private final StudentCourseStatusRepository statusRepository;

  @Autowired
  public StudentCourseStatusService(StudentCourseStatusRepository repository) {
    this.statusRepository = repository;
  }

  /**
   * 現在のステータスを取得する
   */
  public StudentCourseStatus searchStudentCourseId(Integer studentCourseId) {
    return statusRepository.searchStudentCourseId(studentCourseId);
  }

  /**
   * ステータスを更新する
   */
  @Transactional
  public void updateStatus(Integer studentCourseId, String newStatus) {
    StudentCourseStatus current = statusRepository.searchStudentCourseId(studentCourseId);
    if (current == null) {
      throw new IllegalArgumentException(
          "指定されたコースIDのステータスが存在しません" + studentCourseId);
    }

    if (!STATUS_1.equals(newStatus)
        && !STATUS_2.equals(newStatus)
        && !STATUS_3.equals(newStatus)
        && !STATUS_4.equals(newStatus)) {
      throw new IllegalArgumentException("ステータスの値が不正です:" + newStatus);
    }

    current.setStatus(newStatus);
    statusRepository.updateStudentCourseStatus(current);
  }

  /**
   * 受講生コースに初期ステータス(仮申込)を登録する。
   */
  @Transactional
  public void registerInitStatus(Integer studentCourseId) {
    StudentCourseStatus status = new StudentCourseStatus();
    status.setStudentCourseId(studentCourseId);
    status.setStatus(STATUS_1);

    statusRepository.registerStudentCourseStatus(status);
  }

}
