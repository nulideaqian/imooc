package com.imooc.flink.partitioner;

import org.apache.flink.api.common.functions.Partitioner;

/**
 * @author Galaxy
 * @since 2022/3/9 0:41
 */
public class PkPartitioner implements Partitioner<String> {

  @Override
  public int partition(String key, int numPartitions) {
    System.out.println("numPartitions: " + numPartitions);
    if ("imooc.com".equals(key)) {
      return 0;
    } else if ("a.com".equals(key)) {
      return 1;
    }
    return 2;
  }
}
