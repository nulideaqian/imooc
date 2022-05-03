package org.galaxy.creational.pattern.prototype.clone;

import java.util.Date;

/**
 * @author Galaxy
 * @since 2022/5/3 23:16
 */
public class Test {

  public static void main(String[] args) throws CloneNotSupportedException {
    Date birthDay = new Date(0L);
    Pig pig1 = new Pig("佩奇", birthDay);
    Pig pig2 = (Pig) pig1.clone();
    System.out.println(pig1);
    System.out.println(pig2);

    pig1.getBirthDay().setTime(66666666L);
    System.out.println(pig1);
    System.out.println(pig2);
  }

}
