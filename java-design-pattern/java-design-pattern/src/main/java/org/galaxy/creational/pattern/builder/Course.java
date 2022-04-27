package org.galaxy.creational.pattern.builder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Galaxy
 * @since 2022/4/28 1:31
 */
@Getter
@Setter
@ToString
public class Course {

  private String courseName;

  private String coursePpt;

  private String courseVideo;

  private String courseArticle;

  private String courseQa;

  private Course(Builder builder) {
    setCourseName(builder.courseName);
    setCourseName(builder.courseName);
    setCourseName(builder.courseName);
    setCourseName(builder.courseName);
    setCourseName(builder.courseName);
  }

  public static class Builder {

    private String courseName;

    private String coursePpt;

    private String courseVideo;

    private String courseArticle;

    private String courseQa;

    public Builder courseName(String courseName) {
      this.courseName = courseName;
      return this;
    }

    public Builder coursePpt(String coursePpt) {
      this.coursePpt = coursePpt;
      return this;
    }

    public Builder courseVideo(String courseVideo) {
      this.courseVideo = courseVideo;
      return this;
    }

    public Builder courseArticle(String courseArticle) {
      this.courseArticle = courseArticle;
      return this;
    }

    public Builder courseQa(String courseQa) {
      this.courseQa = courseQa;
      return this;
    }

    public Course build() {
      return new Course(this);
    }

  }

}
