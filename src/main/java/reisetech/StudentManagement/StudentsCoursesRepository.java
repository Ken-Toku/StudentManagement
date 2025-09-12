package reisetech.studentmanagement;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StudentsCoursesRepository {

  @Select("SELECT * FROM students_courses")
  List<StudentsCourses> search();
}
