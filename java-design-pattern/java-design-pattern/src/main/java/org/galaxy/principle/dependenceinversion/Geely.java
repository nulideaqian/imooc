package org.galaxy.principle.dependenceinversion;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Galaxy
 * @since 2022/4/25 23:58
 */
@Slf4j
public class Geely {
  private  ICourse iCourse;

  public Geely(ICourse iCourse) {
    this.iCourse = iCourse;
  }

  public void studyImoocCourse() {
    this.iCourse.studyCourse();
  }

}
