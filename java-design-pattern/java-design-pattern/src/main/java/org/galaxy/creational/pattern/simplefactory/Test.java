package org.galaxy.creational.pattern.simplefactory;

/**
 * @author Galaxy
 * @since 2022/4/26 23:21
 */
public class Test {

  public static void main(String[] args) {
    VideoFactory videoFactory = new VideoFactory();
    Video video = videoFactory.getVideo(JavaVideo.class);
    video.produce();
  }

}
