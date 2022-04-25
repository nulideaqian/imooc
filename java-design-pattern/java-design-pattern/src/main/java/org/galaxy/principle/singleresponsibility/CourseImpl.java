package org.galaxy.principle.singleresponsibility;

/**
 * @author Galaxy
 * @since 2022/4/26 0:47
 */
public class CourseImpl implements ICourseManager, ICourseContent {

  @Override
  public String getCourseName() {
    return null;
  }

  @Override
  public byte[] getCourseVideo() {
    return new byte[0];
  }

  @Override
  public void studyCourse() {

  }

  @Override
  public void refundCourse() {

  }
}
