package org.galaxy.principle.dependenceinversion;

/**
 * @author Galaxy
 * @since 2022/4/25 23:59
 */
public class Test {

  public static void main(String[] args) {
    Geely geely = new Geely(new JavaCourse());
    geely.studyImoocCourse();
  }

}
