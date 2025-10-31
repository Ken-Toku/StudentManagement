package reisetech.studentmanagement.date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {

  private Integer id;
  private String name;
  private String furigana;
  private int age;
  private String gender;
  private String nickname;
  private String email;
  private String city;
  private String remark;
  private boolean isDeleted;

}
