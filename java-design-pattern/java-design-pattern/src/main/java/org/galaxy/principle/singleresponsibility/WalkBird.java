package org.galaxy.principle.singleresponsibility;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Galaxy
 * @since 2022/4/26 0:42
 */
@Slf4j
public class WalkBird {

  public void mainMoveMode(String birdName) {
    log.info("{} 用脚走", birdName);
  }

}
