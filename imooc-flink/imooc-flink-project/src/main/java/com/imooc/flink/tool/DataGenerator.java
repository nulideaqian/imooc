package com.imooc.flink.tool;

import com.alibaba.fastjson.JSON;
import com.imooc.flink.domain.Access;
import com.imooc.flink.domain.Product;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.apache.commons.io.FileUtils;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple7;

/**
 * @author Galaxy
 * @since 2022/3/13 13:02
 */
public class DataGenerator {

  private static Random random = new Random();

  public static void main(String[] args) throws IOException {
    File accessLog = new File("data/access.json");
    FileUtils.write(accessLog, "", StandardCharsets.UTF_8);
    for (int i = 0; i < 7; i++) {
      Access access = new Access();
      updateUserStartup(access, i);
      FileUtils.write(accessLog, JSON.toJSONString(access) + System.lineSeparator(),
          StandardCharsets.UTF_8, true);
    }
    for (int i = 0; i < 10000; i++) {
      Access access = new Access();
      updateUserStartup(access, random.nextInt(7));
      updateUserCart(access);
      FileUtils.write(accessLog, JSON.toJSONString(access) + System.lineSeparator(),
          StandardCharsets.UTF_8, true);
    }
  }

  private static void updateUserStartup(Access access, int index) {
    List<Tuple7<String, String, String, String, String, Integer, String>> users = Arrays.asList(
        Tuple7.of("8fe721d6-cd41-4869-a14d-23b4fbfc70df", "iPhone 10", "iOS", "5G", "user_1", 1,
            "V1.2.2"),
        Tuple7.of("c041bc98-55c2-4fad-b4a4-ad0c79a11b78", "iPhone 10", "iOS", "5G", "user_2", 0,
            "V1.2.4"),
        Tuple7.of("c041bc98-55c2-4fad-b4a4-ad0c79a11b78", "iPhone 8", "iOS", "4G", "user_3", 0,
            "V1.2.4"),
        Tuple7.of("cad4d506-1552-4137-8c9a-067156791777", "小米10", "Android", "5G", "user_4",
            1, "V1.2.3"),
        Tuple7.of("525a230a-aebd-4999-89a7-ee7558603cfc", "小米11 Ultra", "Android", "5G", "user_5",
            1, "V1.2.4"),
        Tuple7.of("525a230a-aebd-4444-89a7-ee7558603cfd", "小米11 Ultra", "Android", "5G", "user_6",
            1, "V1.2.2"),
        Tuple7.of("525a230a-aebd-3333-89a7-ee7558603cfe", "小米11 Ultra", "Android", "5G", "user_7",
            0, "V1.2.3"));
    Tuple7<String, String, String, String, String, Integer, String> user = users.get(index);
    access.device = user.f0;
    access.deviceType = user.f1;
    access.os = user.f2;
    access.net = user.f3;
    access.uid = user.f4;
    access.nu = user.f5;
    access.version = user.f6;
    access.event = "startup";
    access.ip = "114.247.50.2";
    access.time = LocalDateTime.now().toInstant(ZoneOffset.UTC).getEpochSecond();
    List<String> channels = Arrays.asList("华为商城", "Appstore");
    access.channel = channels.get(random.nextInt(channels.size()));
  }

  private static void updateUserCart(Access access) {
    List<Tuple2<String, String>> products = Arrays.asList(Tuple2.of("宝马", "车"),
        Tuple2.of("矿泉水", "食品饮料"),
        Tuple2.of("保时捷", "车"),
        Tuple2.of("运动包", "户外"),
        Tuple2.of("Spark进阶大数据离线与实时项目开发", "大数据实战"),
        Tuple2.of("奔驰", "车"),
        Tuple2.of("实战Spark3实时处理掌握两套企业级处理方案", "大数据实战"),
        Tuple2.of("咖啡", "视频饮料"),
        Tuple2.of("帆布鞋", "户外"),
        Tuple2.of("五子", "视频饮料"),
        Tuple2.of("奥迪", "车"));
    Tuple2<String, String> product = products.get(random.nextInt(products.size()));
    access.product = new Product();
    access.product.name = product.f0;
    access.product.category = product.f1;
    List<String> events = Arrays.asList("pay", "browse", "browse", "browse", "browse", "browse",
        "cart", "cart", "cart");
    access.event = events.get(random.nextInt(events.size()));
  }
}
