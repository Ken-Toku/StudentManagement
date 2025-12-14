package reisetech.studentmanagement.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * アプリ全体の共通エラー処理クラスです。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  private Map<String, Object> buildErrorBody(
      HttpStatus status,
      String message,
      String path) {

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now().toString());
    body.put("status", status.value());
    body.put("error", status.getReasonPhrase());
    body.put("message", message);
    body.put("path", path);
    return body;
  }

  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Map<String, Object> handleRuntimeException(
      RuntimeException ex,
      HttpServletRequest request) {

    return buildErrorBody(
        HttpStatus.INTERNAL_SERVER_ERROR,
        ex.getMessage(),
        request.getRequestURI());
  }

  /**
   * 存在しないリソースにアクセスした場合（404）
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Map<String, Object> handleResourceNotFoundException(
      ResourceNotFoundException ex,
      HttpServletRequest request) {

    return buildErrorBody(
        HttpStatus.NOT_FOUND,
        ex.getMessage(),
        request.getRequestURI());
  }

  /**
   * リクエストの内容が不正な場合(400) 例：想定外のステータス値等
   */
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, Object> handleIllegalArgumentException(
      IllegalArgumentException ex,
      HttpServletRequest request) {

    return buildErrorBody(
        HttpStatus.BAD_REQUEST,
        ex.getMessage(),
        request.getRequestURI());
  }

  /**
   * RequestBodyの@Valid失敗
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)

  public Map<String, Object> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex,
      HttpServletRequest request) {

    HttpStatus status = HttpStatus.BAD_REQUEST;

    Map<String, Object> body = buildErrorBody(
        status,
        "入力値が不正です。",
        request.getRequestURI());

    List<Map<String, String>> fieldErrors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(error -> {
          Map<String, String> fe = new LinkedHashMap<>();
          fe.put("field", error.getField());
          fe.put("message", error.getDefaultMessage());
          return fe;
        })
        .toList();

    body.put("fieldErrors", fieldErrors);
    return body;
  }

  /**
   * @PathVariable / @RequestParam の制約違反
   */
  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, Object> handleConstraintViolationException(
      ConstraintViolationException ex,
      HttpServletRequest request) {

    HttpStatus status = HttpStatus.BAD_REQUEST;

    Map<String, Object> body = buildErrorBody(
        status,
        "パラメータが不正です。",
        request.getRequestURI());

    List<Map<String, String>> violations = ex.getConstraintViolations()
        .stream()
        .map(violation -> {
          Map<String, String> v = new LinkedHashMap<>();
          v.put("field", violation.getPropertyPath().toString());
          v.put("message", violation.getMessage());
          return v;
        })
        .toList();

    body.put("violations", violations);
    return body;
  }

}
