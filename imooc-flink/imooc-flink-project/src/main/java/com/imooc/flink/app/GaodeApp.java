package com.imooc.flink.app;

import cn.hutool.http.HttpUtil;

/**
 * @author Galaxy
 * @since 2022/3/13 18:11
 */
public class GaodeApp {

  public static void main(String[] args) {
    String ip = "114.247.50.2";
    String url = "https://restapi.amap.com/v3/ip?ip=114.247.50.2&output=json&key=e7df2d542ccb4547b3a2d6be172bdd55";
    String result1= HttpUtil.get(url);
    System.out.println(result1);
  }

}
