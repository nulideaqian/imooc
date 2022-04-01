package com.imooc.flink.state;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.guava30.com.google.common.collect.Lists;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @author Galaxy
 * @since 2022/3/23 0:36
 */
public class StateApp {

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    test01(env);
    env.execute("state app");
  }

  /**
   * 使用ValuteState
   *
   * @param env env
   */
  private static void test01(StreamExecutionEnvironment env) {
    List<Tuple2<Long, Long>> list = new ArrayList<>();
    list.add(Tuple2.of(1L, 3L));
    list.add(Tuple2.of(1L, 7L));
    list.add(Tuple2.of(2L, 4L));
    list.add(Tuple2.of(1L, 5L));
    list.add(Tuple2.of(2L, 2L));
    list.add(Tuple2.of(2L, 5L));
    list.add(Tuple2.of(1L, 3L));
    env.fromCollection(list)
        .keyBy(x -> x.f0)
        .flatMap(new AvgWithValueState())
        .print();
  }
}

class AvgWithMapState extends RichFlatMapFunction<Tuple2<Long, Long>, Tuple2<Long, Double>> {

  private transient MapState<String, Long> mapState;

  @Override
  public void open(Configuration parameters) throws Exception {
    MapStateDescriptor<String, Long> descriptor = new MapStateDescriptor<>("avg",
        String.class, Long.class);
    mapState = getRuntimeContext().getMapState(descriptor);
  }

  @Override
  public void flatMap(Tuple2<Long, Long> value, Collector<Tuple2<Long, Double>> out)
      throws Exception {
    mapState.put(UUID.randomUUID().toString(), value.f1);
    ArrayList<Long> elements = Lists.newArrayList(mapState.values());
    if (elements.size() == 2) {
      Long count = 0L;
      Long sum = 0L;
      for (Long element : elements) {
        count++;
        sum += element;
      }
      out.collect(Tuple2.of(value.f0, sum / count.doubleValue()));
      mapState.clear();
    }
  }
}

class AvgWithValueState extends RichFlatMapFunction<Tuple2<Long, Long>, Tuple2<Long, Long>> {

  private transient ValueState<Tuple2<Long, Long>> sum;

  private transient ValueState<Tuple2<Long, Long>> newest;

  @Override
  public void open(Configuration parameters) throws Exception {
    ValueStateDescriptor<Tuple2<Long, Long>> descriptor = new ValueStateDescriptor<>(
        "avg", Types.TUPLE(Types.LONG, Types.LONG));
    ValueStateDescriptor<Tuple2<Long, Long>> descriptorNew = new ValueStateDescriptor<Tuple2<Long, Long>>(
        "newest", Types.TUPLE(Types.LONG, Types.LONG));
    sum = getRuntimeContext().getState(descriptor);
    newest = getRuntimeContext().getState(descriptorNew);
  }

  @Override
  public void flatMap(Tuple2<Long, Long> value, Collector<Tuple2<Long, Long>> out)
      throws Exception {
    Tuple2<Long, Long> currentState = sum.value();
    Tuple2<Long, Long> currentStateNew = newest.value();
    if (currentState == null) {
      currentState = Tuple2.of(0L, 0L);
    }
    if (currentStateNew == null) {
      currentStateNew = Tuple2.of(0L, 0L);
    }
    currentState.f0 += 1;
    currentState.f1 += value.f1;
    currentStateNew.f0 = value.f0;
    currentStateNew.f1 = value.f1;
    sum.update(currentState);
    newest.update(currentStateNew);

    out.collect(sum.value());
    out.collect(newest.value());
  }
}