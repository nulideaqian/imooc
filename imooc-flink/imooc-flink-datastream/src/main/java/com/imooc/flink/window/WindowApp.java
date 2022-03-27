package com.imooc.flink.window;

import java.util.List;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction.Context;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * @author Galaxy
 * @since 2022/3/16 1:03
 */
public class WindowApp {

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    test04(env);
    env.execute("Window App");
  }

  private static void test04(StreamExecutionEnvironment env) {
    env.socketTextStream("localhost", 9527)
        .map(new MapFunction<String, Tuple2<String, Integer>>() {
          @Override
          public Tuple2<String, Integer> map(String value) throws Exception {
            return Tuple2.of("pk", Integer.parseInt(value.trim()));
          }
        })
        .keyBy(x -> x.f0)
        .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
        .process(new ProcessWindowFunction<Tuple2<String, Integer>, Integer, String, TimeWindow>() {
          @Override
          public void process(String s,
              ProcessWindowFunction<Tuple2<String, Integer>, Integer, String, TimeWindow>.Context context,
              Iterable<Tuple2<String, Integer>> elements, Collector<Integer> out) throws Exception {
            int max = Integer.MIN_VALUE;
            for (Tuple2<String, Integer> element : elements) {
              max = Math.max(element.f1, max);
            }
            out.collect(max);
          }
        })
        .print();
  }

  private static void test03(StreamExecutionEnvironment env) {
    env.socketTextStream("localhost", 9527)
        .map(new MapFunction<String, Tuple2<String, Integer>>() {
          @Override
          public Tuple2<String, Integer> map(String value) throws Exception {
            return Tuple2.of("pk", Integer.parseInt(value));
          }
        })
        .keyBy(x -> x.f0)
        .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
        .process(new ProcessWindowFunction<Tuple2<String, Integer>, String, String, TimeWindow>() {
          @Override
          public void process(String s,
              ProcessWindowFunction<Tuple2<String, Integer>, String, String, TimeWindow>.Context context,
              Iterable<Tuple2<String, Integer>> elements, Collector<String> out) throws Exception {
            int max = 0;
            for (Tuple2<String, Integer> element : elements) {
              max = Math.max(max, element.f1);
            }
            out.collect("max value is : " + max);
          }
        })
        .print();
  }

  private static void test02(StreamExecutionEnvironment env) {
    env.socketTextStream("localhost", 9527)
        .map(new MapFunction<String, Tuple2<String, Integer>>() {
          @Override
          public Tuple2<String, Integer> map(String value) throws Exception {
            String[] splits = value.trim().split(",");
            return Tuple2.of(splits[0].trim(), Integer.parseInt(splits[1].trim()));
          }
        })
        .keyBy(x -> x.f0)
        .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
        .reduce(new ReduceFunction<Tuple2<String, Integer>>() {
          @Override
          public Tuple2<String, Integer> reduce(Tuple2<String, Integer> value1,
              Tuple2<String, Integer> value2) throws Exception {
            return Tuple2.of(value1.f0, value1.f1 + value2.f1);
          }
        })
        .print();
  }

  private static void test01(StreamExecutionEnvironment env) {
    // env.socketTextStream("localhost", 9527)
    //     .map(new MapFunction<String, Integer>() {
    //       @Override
    //       public Integer map(String value) throws Exception {
    //         return Integer.parseInt(value);
    //       }
    //     })
    //     .windowAll(TumblingProcessingTimeWindows.of(Time.seconds(5)))
    //     .sum(0)
    //     .print();
    // spark,1 hadoop,2
    env.socketTextStream("localhost", 9527)
        .map(new MapFunction<String, Tuple2<String, Integer>>() {
          @Override
          public Tuple2<String, Integer> map(String value) throws Exception {
            String[] splits = value.trim().split(",");
            return Tuple2.of(splits[0].trim(), Integer.parseInt(splits[1].trim()));
          }
        })
        .keyBy(x -> x.f0)
        .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
        .sum(1)
        .print();
  }

}
