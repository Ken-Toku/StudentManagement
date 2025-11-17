package reisetech.studentmanagement.date;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生")
@Getter
@Setter
public class Student {

  private Integer id;

  @NotBlank(message = "名前は必須です")
  @Size(max = 50, message = "名前は50文字以内で入力してください")
  private String name;

  @NotBlank(message = "ふりがなは必須です")
  @Size(max = 50, message = "ふりがなは50文字以内で入力してください")
  private String furigana;

  @NotNull(message = "年齢は必須です")
  @Min(value = 0, message = "年齢は0以上で入力してください")
  @Max(value = 100, message = "年齢は100以下で入力してください")
  private Integer age;

  @NotBlank(message = "性別は必須です")
  @Size(max = 10, message = "性別は10文字以内で入力してください")
  private String gender;

  @Size(max = 20, message = "ニックネームは20文字以内で入力してください")
  private String nickname;

  @NotBlank(message = "メールアドレスは必須です")
  @Email(message = "メールアドレスの形式が不正です")
  @Size(max = 100, message = "メールアドレスは100文字以内で入力してください")
  private String email;

  @NotBlank(message = "都道府県は必須です")
  @Size(max = 10, message = "都道府県は10文字以内で入力してください")
  private String city;

  @Size(max = 200, message = "備考は200文字以内で入力してください")
  private String remark;

  private boolean isDeleted;

}
