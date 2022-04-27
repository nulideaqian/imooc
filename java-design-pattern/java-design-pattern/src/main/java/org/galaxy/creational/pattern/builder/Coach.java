package org.galaxy.creational.pattern.builder;

/**
 * @author Galaxy
 * @since 2022/4/28 1:38
 */
public class Coach {

  private CourseBuilder courseBuilder;

  public void setCourseBuilder(CourseBuilder courseBuilder) {
    this.courseBuilder = courseBuilder;
  }

  public Course makeCourse(String courseName, String coursePpt, String courseVideo, String courseArticle, String courseQa) {
    this.courseBuilder.buildCourseName(courseName);
    this.courseBuilder.buildCoursePpt(coursePpt);
    this.courseBuilder.buildCourseVideo(courseVideo);
    this.courseBuilder.buildCourseArticle(courseArticle);
    this.courseBuilder.buildCourseQa(courseQa);
    return this.courseBuilder.makeCourse();
  }

}
