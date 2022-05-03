package org.galaxy.creational.pattern.singleton;

/**
 * @author Galaxy
 * @since 2022/5/3 22:15
 */
public class ThreadLocalInstance {

  private static final ThreadLocal<ThreadLocalInstance> threadLocalInstance = new ThreadLocal<>() {
    @Override
    protected ThreadLocalInstance initialValue() {
      return new ThreadLocalInstance();
    }
  };

  private ThreadLocalInstance() {}

  public static ThreadLocalInstance getInstance() {
    return threadLocalInstance.get();
  }

}
