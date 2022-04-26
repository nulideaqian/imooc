package org.galaxy.creational.pattern.simplefactory;

/**
 * @author Galaxy
 * @since 2022/4/26 23:23
 */
public class VideoFactory {

  // public Video getVideo(String type) {
  //   if ("java".equalsIgnoreCase(type)) {
  //     return new JavaVideo();
  //   } else if ("python".equalsIgnoreCase(type)) {
  //     return new PythonVideo();
  //   }
  //   return null;
  // }

  public Video getVideo(Class c) {
    Video video = null;
    try {
      video = (Video) Class.forName(c.getName()).newInstance();
    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    return video;
  }
}
