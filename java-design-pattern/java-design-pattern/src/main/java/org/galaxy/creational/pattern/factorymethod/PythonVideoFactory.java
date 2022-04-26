package org.galaxy.creational.pattern.factorymethod;

/**
 * @author Galaxy
 * @since 2022/4/27 0:32
 */
public class PythonVideoFactory extends VideoFactory {

  @Override
  public Video getVideo() {
    return new PythonVideo();
  }
}
