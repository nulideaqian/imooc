package org.galaxy.creational.pattern.simplefactory;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Galaxy
 * @since 2022/4/26 23:21
 */
@Slf4j
public class PythonVideo extends Video {

  @Override
  public void produce() {
    log.info("录制Python视频");
  }
}
