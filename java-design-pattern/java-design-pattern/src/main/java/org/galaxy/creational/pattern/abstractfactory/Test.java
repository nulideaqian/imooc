package org.galaxy.creational.pattern.abstractfactory;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Galaxy
 * @since 2022/4/28 1:09
 */
@Slf4j
public class Test {

  public static void main(String[] args) {
    CourseFactory courseFactory = new JavaCourseFactory();
    Video video = courseFactory.getVideo();
    Article article = courseFactory.getArticle();
    video.produce();
    article.produce();
  }

}
