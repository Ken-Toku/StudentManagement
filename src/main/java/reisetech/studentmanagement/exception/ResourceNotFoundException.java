package reisetech.studentmanagement.exception;


/**
 * 指定されたリソースが存在しない場合に使用する例外クラスです。
 */
public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String message) {
    super(message);
  }


}
