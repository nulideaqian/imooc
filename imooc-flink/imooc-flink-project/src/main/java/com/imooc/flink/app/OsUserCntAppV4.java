package com.imooc.flink.app;

import com.alibaba.fastjson.JSON;
import com.imooc.flink.domain.Access;
import java.util.Objects;
import javafx.scene.effect.Bloom;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.guava30.com.google.common.hash.BloomFilter;
import org.apache.flink.shaded.guava30.com.google.common.hash.Funnels;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction.Context;
import org.apache.flink.util.Collector;

/**
 * @author Galaxy
 * @since 2022/3/13 12:45
 */
@Slf4j
public class OsUserCntAppV4 {

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   * @throws Exception the exception
   */
  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    env.socketTextStream("localhost", 9527)
        .map(new MapFunction<String, Access>() {
          @Override
          public Access map(String value) {
            try {
              return JSON.parseObject(value, Access.class);
            } catch (Exception e) {
              log.error("access.json error transform to Access, msg is: ", e);
              return null;
            }
          }
        })
        .filter(Objects::nonNull)
        .filter(new FilterFunction<Access>() {
          @Override
          public boolean filter(Access value) throws Exception {
            return "startup".equals(value.event);
          }
        })
        .keyBy(x -> x.deviceType)
        .process(new KeyedProcessFunction<String, Access, Access>() {
          private transient ValueState<BloomFilter<String>> valueState;
          @Override
          public void open(Configuration parameters) throws Exception {
            ValueStateDescriptor<BloomFilter<String>> descriptor = new ValueStateDescriptor<>("s",
                TypeInformation.of(
                    new TypeHint<BloomFilter<String>>() {
                    }));
            valueState = getRuntimeContext().getState(descriptor);
          }

          @Override
          public void processElement(Access value,
              KeyedProcessFunction<String, Access, Access>.Context ctx, Collector<Access> out)
              throws Exception {
            String device = value.device;
            BloomFilter<String> bloomFilter = valueState.value();
            if (bloomFilter == null) {
              bloomFilter = BloomFilter.create(Funnels.unencodedCharsFunnel(), 100000);
            }
            if (!bloomFilter.mightContain(device)) {
              bloomFilter.put(device);
              value.nu2 = 1;
              valueState.update(bloomFilter);
            }
            out.collect(value);
          }
        })
        .print()
        .setParallelism(1);

    env.execute("OsUserCntAppV1");
  }
}
