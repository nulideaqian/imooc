package com.imooc.flink.basic;

import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * @author Galaxy
 * @since 2022/2/21 0:02
 */
public class BatchWcApp {

  public static void main(String[] args) throws Exception {
    ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
    DataSource<String> dataSource = env.readTextFile("data/wc.data");
    dataSource.flatMap(new FlatMapFunction<String, String>() {
      @Override
      public void flatMap(String value, Collector<String> collector) throws Exception {
        String[] words = value.split(",");
        for (String word : words) {
          collector.collect(word.toLowerCase(Locale.ROOT).trim());
        }
      }
    }).filter(new FilterFunction<String>() {
      @Override
      public boolean filter(String value) throws Exception {
        return StringUtils.isNotEmpty(value);
      }
    }).map(new MapFunction<String, Tuple2<String, Integer>>() {
      @Override
      public Tuple2<String, Integer> map(String value) throws Exception {
        return new Tuple2<>(value, 1);
      }
    }).groupBy(0).sum(1).print();
  }
}
