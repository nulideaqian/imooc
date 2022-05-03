package org.galaxy.creational.pattern.singleton;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Galaxy
 * @since 2022/5/3 19:45
 */
public class HungrySingleton implements Serializable {

  private final static HungrySingleton hungrySingleton = new HungrySingleton();

  private HungrySingleton() {
    if (hungrySingleton != null) {
      throw new RuntimeException("单例构造器禁止调用");
    }
  }

  public static HungrySingleton getInstance() {
    return hungrySingleton;
  }

  @Serial
  private Object readResolve() {
    return hungrySingleton;
  }

}
