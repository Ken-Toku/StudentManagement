package reisetech.studentmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reisetech.studentmanagement.data.StudentCourseStatus;
import reisetech.studentmanagement.domain.StudentDetail;
import reisetech.studentmanagement.service.StudentCourseStatusService;
import reisetech.studentmanagement.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして実行されるControllerです。
 */

@RestController
@Validated
public class StudentController {

  private StudentService service;
  private StudentCourseStatusService statusService;


  @Autowired
  public StudentController(StudentService service, StudentCourseStatusService statusService) {

    this.service = service;
    this.statusService = statusService;
  }

  /**
   * 受講生詳細の一覧検索です。 全件検索を行うので、条件指定は行いません。
   *
   * @return　受講生詳細一覧（全件）
   */
  @Operation(summary = "受講生一覧の取得", description = "登録済みの受講生情報を全件取得します。条件指定はありません。")
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    return service.searchStudentList();
  }

  /**
   * 受講生詳細の検索です。 IDに紐づく任意の受講生の情報を取得します。
   *
   * @param  　id　受講生ID
   * @return　受講生情報
   */
  @Operation(summary = "受講生詳細の取得", description = "受講生IDを指定して、該当する受講生の詳細情報を１件取得します。"

  )
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(
      @Parameter(description = "受講生ＩＤ（1～999の整数）") @PathVariable @Min(value = 1, message = "IDは1以上の数値を指定してください") @Max(value = 999, message = "IDは3桁までの数値を指定してください") Integer id) {
    return service.searchStudent(id);
  }

  /**
   * 受講生詳細の登録を行います。
   *
   * @param ? 　studentDetailもしくはbindingResult
   * @return　実行結果
   */
  @Operation(summary = "受講生の新規登録", description = "受講生詳細情報（StudentDetailを受け取り、新規に登録します。")
  @PostMapping("/registerStudent")
  public ResponseEntity<?> registerStudent(@RequestBody @Valid StudentDetail studentDetail) {

    StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  /**
   * 受講生詳細の更新を行います。 キャンセルフラグの更新もここで行います。（論理削除）
   *
   * @param ? studentDetailかbindingResult
   * @return　実行結果
   */
  @Operation(summary = "受講生情報の更新", description = "既存の受講生情報を更新します。キャンセルフラグの更新（論理削除）も含みます。")
  @PutMapping("/updateStudent")
  public ResponseEntity<?> updateStudent(@RequestBody @Valid StudentDetail studentDetail) {

    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }

  @GetMapping("/exception")
  public ResponseEntity<String> throwException() throws NotFoundException {
    throw new NotFoundException("このAPIは現在利用できません。古いURLとなっています。");
  }
  
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }

  /**
   * コースステータスの更新を行います。
   *
   * @param courseStatus コースIDとステータス
   * @return　実行結果メッセージ
   */
  @Operation(summary = "コースステータスの更新", description = "受講生コース情報のステータスの更新します。")
  @PutMapping("/updateCourseStatus")
  public ResponseEntity<?> updateCourseStatus(@RequestBody StudentCourseStatus courseStatus) {
    statusService.updateStatus(courseStatus.getStudentCourseId(), courseStatus.getStatus());

    return ResponseEntity.ok("ステータスの更新処理が成功しました。");
  }
}
