package reisetech.studentmanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 例外ハンドリングの動作確認用コントローラー.
 */
@RestController
public class DebugContriller {

  /**
   * 共通エラー処理の動作確認用。 このURLにアクセスすると、あえて例外を発生させます。
   */
  @GetMapping("/testError")
  public String testError() {
    throw new RuntimeException("テスト用の例外です。");
  }
}
