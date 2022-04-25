package org.galaxy.principle.singleresponsibility;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Galaxy
 * @since 2022/4/26 0:37
 */
@Slf4j
public class Bird {

  public void mainMoveMode(String name) {
    log.info("{} 用翅膀飞", name);
  }

}
