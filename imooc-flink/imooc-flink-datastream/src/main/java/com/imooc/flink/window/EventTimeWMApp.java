package com.imooc.flink.window;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * @author Galaxy
 * @since 2022/3/20 23:39
 */
public class EventTimeWMApp {

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    test01(env);
    env.execute("app run");
  }

  private static void test01(StreamExecutionEnvironment env) {
    OutputTag<Tuple2<String, Integer>> outputTag = new OutputTag<Tuple2<String, Integer>>("late-tag"){};
    env.setParallelism(1);
    SingleOutputStreamOperator<String> window = env.socketTextStream("localhost", 9527)
        .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<String>(
            Time.seconds(0)) {
          @Override
          public long extractTimestamp(String element) {
            return Long.parseLong(element.split(",")[0]);
          }
        })
        .map(new MapFunction<String, Tuple2<String, Integer>>() {
          @Override
          public Tuple2<String, Integer> map(String value) throws Exception {
            String[] splits = value.split(",");
            return Tuple2.of(splits[1], Integer.parseInt(splits[2]));
          }
        })
        .keyBy(x -> x.f0)
        .window(TumblingEventTimeWindows.of(Time.seconds(5)))
        .sideOutputLateData(outputTag)
        .reduce(new ReduceFunction<Tuple2<String, Integer>>() {
          @Override
          public Tuple2<String, Integer> reduce(Tuple2<String, Integer> value1,
              Tuple2<String, Integer> value2) throws Exception {
            System.out.println(
                "----reduce invoked----" + value1.f0 + " " + value1.f1 + " " + value2.f1);
            return Tuple2.of(value1.f0, value1.f1 + value2.f1);
          }
        }, new ProcessWindowFunction<Tuple2<String, Integer>, String, String, TimeWindow>() {
          @Override
          public void process(String s,
              ProcessWindowFunction<Tuple2<String, Integer>, String, String, TimeWindow>.Context context,
              Iterable<Tuple2<String, Integer>> elements, Collector<String> out) throws Exception {
            for (Tuple2<String, Integer> element : elements) {
              out.collect(
                  "[" + DateFormatUtils.format(context.window().getStart(), "yyyy-MM-dd HH:mm:ss")
                      + " -- " + DateFormatUtils.format(context.window().getEnd(),
                      "yyyy-MM-dd HH:mm:ss"));
            }
          }
        });
    window.print();

    DataStream<Tuple2<String, Integer>> sideOutput = window.getSideOutput(outputTag);
    sideOutput.printToErr();
  }

}
