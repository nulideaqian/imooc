package org.galaxy.creational.pattern.prototype.abstractprototype;

/**
 * @author Galaxy
 * @since 2022/5/3 23:13
 */
public abstract class A implements Cloneable {

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
