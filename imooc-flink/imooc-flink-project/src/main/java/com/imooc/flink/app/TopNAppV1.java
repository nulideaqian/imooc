package com.imooc.flink.app;

import com.alibaba.fastjson.JSON;
import com.imooc.flink.domain.Access;
import com.imooc.flink.domain.EventCatagoryProduct;
import com.imooc.flink.udf.TopNAggregateFunction;
import com.imooc.flink.udf.TopNWindowFunction;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.guava30.com.google.common.collect.Lists;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction.Context;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction.OnTimerContext;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

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
        .keyBy(new KeySelector<EventCatagoryProduct, Tuple4<String, String, Long, Long>>() {
          @Override
          public Tuple4<String, String, Long, Long> getKey(EventCatagoryProduct value)
              throws Exception {
            return Tuple4.of(value.event, value.category, value.start, value.end);
          }
        })
        .process(
            new KeyedProcessFunction<Tuple4<String, String, Long, Long>, EventCatagoryProduct, List<EventCatagoryProduct>>() {
              private transient ListState<EventCatagoryProduct> listState;

              @Override
              public void open(Configuration parameters) throws Exception {
                listState = getRuntimeContext().getListState(
                    new ListStateDescriptor<>("cnt-count", EventCatagoryProduct.class));
              }

              @Override
              public void processElement(EventCatagoryProduct value,
                  KeyedProcessFunction<Tuple4<String, String, Long, Long>, EventCatagoryProduct, List<EventCatagoryProduct>>.Context ctx,
                  Collector<List<EventCatagoryProduct>> out) throws Exception {
                listState.add(value);

                // 注册事件
                ctx.timerService().registerEventTimeTimer(value.end + 1);
              }

              // 在这里完成TopN操作
              @Override
              public void onTimer(long timestamp,
                  KeyedProcessFunction<Tuple4<String, String, Long, Long>, EventCatagoryProduct, List<EventCatagoryProduct>>.OnTimerContext ctx,
                  Collector<List<EventCatagoryProduct>> out) throws Exception {
                ArrayList<EventCatagoryProduct> list = Lists.newArrayList(listState.get());
                ArrayList<EventCatagoryProduct> sorted = new ArrayList<>(3);
                list.sort((x, y) -> Long.compare(y.count, x.count));
                for (int i = 0; i < Math.min(3, list.size()); i++) {
                  sorted.add(list.get(i));
                }
                out.collect(sorted);
              }
            })
        .print();
    env.execute();
  }
}
