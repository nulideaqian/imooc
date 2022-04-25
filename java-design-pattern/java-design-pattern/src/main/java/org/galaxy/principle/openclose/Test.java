package org.galaxy.principle.openclose;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Galaxy
 * @since 2022/4/25 23:37
 */
@Slf4j
public class Test {

  public static void main(String[] args) {
    ICourse javaCourse = new JavaDiscountCourse(96, "Java从零到企业级电商开发", 348d);
    log.info("id is {}, name is {}, price is {}", javaCourse.getId(), javaCourse.getName(),
        javaCourse.getPrice());
  }

}
