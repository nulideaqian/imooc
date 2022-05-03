package org.galaxy.creational.pattern.singleton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Galaxy
 * @since 2022/5/3 18:52
 */
public class Test {

  public static void main(String[] args)
      throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    // Thread t1 = new Thread(new T());
    // Thread t2 = new Thread(new T());
    // t1.start();
    // t2.start();
    // System.out.println("program end");

    // HungrySingleton instance = HungrySingleton.getInstance();
    // ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("singleton_file"));
    // oos.writeObject(instance);
    // File file = new File("singleton_file");
    // ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
    // HungrySingleton newInstance = (HungrySingleton) ois.readObject();
    // System.out.println(instance);
    // System.out.println(newInstance);

    Class objectClass = HungrySingleton.class;
    Constructor constructor = objectClass.getDeclaredConstructor();
    constructor.setAccessible(true);
    HungrySingleton instance = HungrySingleton.getInstance();
    HungrySingleton newInstance = (HungrySingleton) constructor.newInstance();
    System.out.println(instance);
    System.out.println(newInstance);
  }

}
