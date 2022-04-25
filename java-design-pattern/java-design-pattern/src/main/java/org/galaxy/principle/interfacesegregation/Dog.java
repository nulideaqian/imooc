package org.galaxy.principle.interfacesegregation;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Galaxy
 * @since 2022/4/26 0:58
 */
@Slf4j
public class Dog implements IEatAnimalAction, ISwimAnimalAction {

  @Override
  public void eat() {

  }

  @Override
  public void swim() {

  }
}
