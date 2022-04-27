package org.galaxy.creational.pattern.abstractfactory;

/**
 * @author Galaxy
 * @since 2022/4/28 1:02
 */
public class JavaCourseFactory implements CourseFactory {

  @Override
  public Video getVideo() {
    return new JavaVideo();
  }

  @Override
  public Article getArticle() {
    return new JavaArticle();
  }
}
