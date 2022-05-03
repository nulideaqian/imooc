package org.galaxy.creational.pattern.singleton;

/**
 * @author Galaxy
 * @since 2022/5/3 21:04
 */
public enum EnumInstance {
  INSTANCE;

  private Object data;

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  public static EnumInstance getInstance() {
    return INSTANCE;
  }
}
