package org.galaxy.creational.pattern.builder;

/**
 * @author Galaxy
 * @since 2022/4/28 1:32
 */
public abstract class CourseBuilder {

  public abstract void buildCourseName(String courseName);

  public abstract void buildCoursePpt(String coursePpt);

  public abstract void buildCourseVideo(String courseVideo);

  public abstract void buildCourseArticle(String courseArticle);

  public abstract void buildCourseQa(String courseQa);

  public abstract Course makeCourse();
}
