package org.galaxy.creational.pattern.prototype.abstractprototype;

/**
 * @author Galaxy
 * @since 2022/5/3 23:13
 */
public class B extends A {

  public static void main(String[] args) throws CloneNotSupportedException {
    B b = new B();
    b.clone();
  }
}
