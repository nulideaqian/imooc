package org.galaxy.creational.pattern.singleton;

/**
 * @author Galaxy
 * @since 2022/5/3 18:54
 */
public class T implements Runnable {

  @Override
  public void run() {
    LazySingleton lazySingleton = LazySingleton.getInstance();
    System.out.println(Thread.currentThread().getName() + "-" + lazySingleton);
  }
}
