package com.imooc.flink.basic;

import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * 第一个基于Flink实时处理快速入门案例
 *
 * @author Galaxy
 * @since 2022/2/20 22:40
 */
public class StreamWcApp {

  public static void main(String[] args) throws Exception {
    // 0. prepare context
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    // 1. datasource
    DataStreamSource<String> source = env.socketTextStream("localhost", 9527);
    // 2. transformation
    source.flatMap(new FlatMapFunction<String, String>() {
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
    }).keyBy(new KeySelector<Tuple2<String, Integer>, String>() {
      @Override
      public String getKey(Tuple2<String, Integer> value) throws Exception {
        return value.f0;
      }
    }).sum(1).print();

    env.execute("Stream WC App");
    // 3. storage
  }
}
