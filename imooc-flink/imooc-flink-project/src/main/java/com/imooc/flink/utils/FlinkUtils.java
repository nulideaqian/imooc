package com.imooc.flink.utils;

import java.io.IOException;
import org.apache.flink.api.java.utils.ParameterTool;

/**
 * @author Galaxy
 * @since 2022/4/4 17:43
 */
public class FlinkUtils {

  public static void main(String[] args) throws IOException {
    ParameterTool tool = ParameterTool.fromPropertiesFile(args[0]);
    String groupId = tool.get("group.id", "test");
    String servers = tool.getRequired("bootstrap.servers");

    System.out.println(groupId);
    System.out.println(servers);
  }

}
