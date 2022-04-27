package org.galaxy.creational.pattern.builder;

/**
 * @author Galaxy
 * @since 2022/4/28 1:35
 */
public class CourseActualBuilder extends CourseBuilder {

  private Course course = new Course();

  @Override
  public void buildCourseName(String courseName) {
    course.setCourseName(courseName);
  }

  @Override
  public void buildCoursePpt(String coursePpt) {
    course.setCoursePpt(coursePpt);
  }

  @Override
  public void buildCourseVideo(String courseVideo) {
    course.setCourseVideo(courseVideo);
  }

  @Override
  public void buildCourseArticle(String courseArticle) {
    course.setCourseArticle(courseArticle);
  }

  @Override
  public void buildCourseQa(String courseQa) {
    course.setCourseQa(courseQa);
  }

  @Override
  public Course makeCourse() {
    return course;
  }
}
