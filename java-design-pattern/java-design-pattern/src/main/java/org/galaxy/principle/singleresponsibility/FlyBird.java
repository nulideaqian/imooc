package org.galaxy.principle.singleresponsibility;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Galaxy
 * @since 2022/4/26 0:41
 */
@Slf4j
public class FlyBird {

  public void mainMoveMode(String birdName) {
    log.info("{} 用翅膀飞", birdName);
  }

}
