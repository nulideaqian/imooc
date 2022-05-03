package org.galaxy.creational.pattern.singleton;

/**
 * @author Galaxy
 * @since 2022/5/3 18:50
 */
public class LazySingleton {

  private static LazySingleton lazySingleton = null;

  private LazySingleton() {}

  public synchronized static LazySingleton getInstance() {
    if (lazySingleton == null) {
      lazySingleton = new LazySingleton();
    }
    return lazySingleton;
  }

}
