package org.galaxy.principle.demeter;

import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Galaxy
 * @since 2022/4/26 1:08
 */
@Slf4j
public class TeamLeader {

  public void checkNumberOfCourses(List<Course> courseList) {
    log.info("在线课程的数量是：{}", courseList.size());
  }

}
