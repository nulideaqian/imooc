package org.galaxy.creational.pattern.singleton;

/**
 * @author Galaxy
 * @since 2022/5/3 19:22
 */
public class LazyDoubleCheckSingleton {

  private volatile static LazyDoubleCheckSingleton lazySingleton = null;

  private LazyDoubleCheckSingleton() {
  }

  public static LazyDoubleCheckSingleton getInstance() {
    if (lazySingleton == null) {
      synchronized (LazyDoubleCheckSingleton.class) {
        if (lazySingleton == null) {
          lazySingleton = new LazyDoubleCheckSingleton();
          // 1. 分配内存
          // 2. init this object
          // 3. set lazy double check singleton to address
        }
      }
    }
    return lazySingleton;
  }

}
