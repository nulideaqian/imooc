package org.galaxy.principle.dependenceinversion;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Galaxy
 * @since 2022/4/26 0:19
 */
@Slf4j
public class FeCourse implements ICourse {

  @Override
  public void studyCourse() {
    log.info("Geely在学习FE课程");
  }
}
