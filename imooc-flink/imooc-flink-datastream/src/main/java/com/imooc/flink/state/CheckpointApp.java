package com.imooc.flink.state;

import java.util.concurrent.TimeUnit;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.runtime.state.hashmap.HashMapStateBackend;
import org.apache.flink.runtime.state.storage.FileSystemCheckpointStorage;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @author Galaxy
 * @since 2022/3/31 23:03
 */
public class CheckpointApp {

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.enableCheckpointing(5000);
    env.setStateBackend(new HashMapStateBackend());
    env.getCheckpointConfig().setCheckpointStorage(new FileSystemCheckpointStorage("file:///checkpoint-dir"));
    // env.getCheckpointConfig().setCheckpointStorage("file:///e/code/github/imooc/imooc-flink/checkpoints");
    env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, Time.of(5, TimeUnit.SECONDS)));
    DataStreamSource<String> source = env.socketTextStream("localhost", 9527);
    source.map(new MapFunction<String, String>() {
          @Override
          public String map(String value) throws Exception {
            if (value.contains("pk")) {
              throw new RuntimeException("error found!");
            } else {
              return value.toLowerCase();
            }
          }
        })
        .flatMap(new FlatMapFunction<String, String>() {
          @Override
          public void flatMap(String value, Collector<String> out) throws Exception {
            String[] splits = value.split(",");
            for (String split : splits) {
              out.collect(split);
            }
          }
        })
        .map(new MapFunction<String, Tuple2<String, Integer>>() {
          @Override
          public Tuple2<String, Integer> map(String value) throws Exception {
            return Tuple2.of(value, 1);
          }
        })
        .keyBy(x -> x.f0)
        .sum(1)
        .print();
    env.execute("");
  }

}
