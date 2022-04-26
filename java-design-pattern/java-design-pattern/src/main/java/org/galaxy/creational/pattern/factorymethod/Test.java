package org.galaxy.creational.pattern.factorymethod;

/**
 * @author Galaxy
 * @since 2022/4/26 23:21
 */
public class Test {

  public static void main(String[] args) {
    VideoFactory videoFactory = new JavaVideoFactory();
    Video video = videoFactory.getVideo();
    video.produce();
  }

}
