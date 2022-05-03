package org.galaxy.creational.pattern.singleton;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Galaxy
 * @since 2022/5/3 21:26
 */
public class ContainerSingleton {

  private static Map<String, Object> singletonMap = new HashMap<>();

  public static void putInstance(String key, Object instance) {
    if (StringUtils.isNotEmpty(key) && instance != null) {
      singletonMap.putIfAbsent(key, instance);
    }
  }

  public static Object getInstance(String key) {
    return singletonMap.get(key);
  }

}
