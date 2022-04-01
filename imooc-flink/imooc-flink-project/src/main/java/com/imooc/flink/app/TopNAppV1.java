package com.imooc.flink.app;

import com.alibaba.fastjson.JSON;
import com.imooc.flink.domain.Access;
import com.imooc.flink.udf.TopNAggregateFunction;
import com.imooc.flink.udf.TopNWindowFunction;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * @author Galaxy
 * @since 2022/4/1 23:45
 */
@Slf4j
public class TopNAppV1 {

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(1);
    env.readTextFile("data/access.json")
        .map(new MapFunction<String, Access>() {
          @Override
          public Access map(String value) throws Exception {
            try {
              return JSON.parseObject(value, Access.class);
            } catch (Exception e) {
              log.error("access.json error transform to Access, msg is: ", e);
              return null;
            }
          }
        })
        .assignTimestampsAndWatermarks(
            WatermarkStrategy.<Access>forBoundedOutOfOrderness(Duration.ofSeconds(0L))
                .withTimestampAssigner((event, timestamp) -> event.time))
        .filter(new FilterFunction<Access>() {
          @Override
          public boolean filter(Access value) throws Exception {
            return !"startup".equals(value.event);
          }
        })
        .keyBy(new KeySelector<Access, Tuple3<String, String, String>>() {
          @Override
          public Tuple3<String, String, String> getKey(Access value) throws Exception {
            return Tuple3.of(value.event, value.product.category, value.product.name);
          }
        })
        .window(SlidingEventTimeWindows.of(Time.minutes(5), Time.minutes(1)))
        .aggregate(new TopNAggregateFunction(), new TopNWindowFunction())
        .print();
    env.execute();
  }
}
