package org.galaxy.principle.demeter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Galaxy
 * @since 2022/4/26 1:08
 */
public class Boss {

  public void commandCheckNumber(TeamLeader teamLeader) {
    List<Course> courseList = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      courseList.add(new Course());
    }
    teamLeader.checkNumberOfCourses(courseList);
  }

}
