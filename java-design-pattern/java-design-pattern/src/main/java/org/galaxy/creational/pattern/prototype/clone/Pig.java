package org.galaxy.creational.pattern.prototype.clone;

import java.util.Date;

/**
 * @author Galaxy
 * @since 2022/5/3 23:15
 */
public class Pig implements Cloneable {

  private String name;

  private Date birthDay;

  public Pig(String name, Date birthDay) {
    this.name = name;
    this.birthDay = birthDay;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getBirthDay() {
    return birthDay;
  }

  public void setBirthDay(Date birthDay) {
    this.birthDay = birthDay;
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    Pig pig = (Pig) super.clone();
    pig.birthDay = (Date) pig.birthDay.clone();
    return pig;
  }

  @Override
  public String toString() {
    return "Pig{" +
        "name='" + name + '\'' +
        ", birthDay=" + birthDay +
        '}' + super.toString();
  }
}
