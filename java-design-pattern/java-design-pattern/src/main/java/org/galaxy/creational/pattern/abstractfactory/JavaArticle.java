package org.galaxy.creational.pattern.abstractfactory;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Galaxy
 * @since 2022/4/28 1:02
 */
@Slf4j
public class JavaArticle extends Article {

  @Override
  public void produce() {
    log.info("录制Java手记");
  }
}
