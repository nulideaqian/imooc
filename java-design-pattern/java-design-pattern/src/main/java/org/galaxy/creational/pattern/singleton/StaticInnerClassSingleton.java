package org.galaxy.creational.pattern.singleton;

/**
 * @author Galaxy
 * @since 2022/5/3 19:37
 */
public class StaticInnerClassSingleton {

  private StaticInnerClassSingleton() {}

  private static class InnerClass {
    private static StaticInnerClassSingleton singleton =new StaticInnerClassSingleton();

    public static StaticInnerClassSingleton getInstance() {
      return singleton;
    }
  }

  public StaticInnerClassSingleton getInstance() {
    return InnerClass.getInstance();
  }

}
