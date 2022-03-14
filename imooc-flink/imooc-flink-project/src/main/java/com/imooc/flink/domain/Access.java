package com.imooc.flink.domain;

import lombok.ToString;

/**
 * @author Galaxy
 * @since 2022/3/13 12:34
 */
@ToString
public class Access {

  public String device;
  /**
   * iPhone 10, 小米11 Ultra
   */
  public String deviceType;
  /**
   * iOS Android
   */
  public String os;
  /**
   * startup pay cart
   */
  public String event;
  /**
   * 4G 5G
   */
  public String net;
  /**
   * 华为商城 Appstore
   */
  public String channel;
  /**
   * user_xx
   */
  public String uid;
  /**
   * 1 新用户
   */
  public int nu;
  /**
   * 10.10.10.xx
   */
  public String ip;
  /**
   * real time
   */
  public long time;
  /**
   * V1.2.2
   */
  public String version;

  public Product product;

  public String province;
}
