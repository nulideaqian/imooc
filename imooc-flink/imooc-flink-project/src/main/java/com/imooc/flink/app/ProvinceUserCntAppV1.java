package com.imooc.flink.app;

import com.alibaba.fastjson.JSON;
import com.imooc.flink.domain.Access;
import com.imooc.flink.udf.GaodeLocationMapFunction;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author Galaxy
 * @since 2022/3/13 12:45
 */
@Slf4j
public class ProvinceUserCntAppV1 {

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   * @throws Exception the exception
   */
  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(1);
    env.readTextFile(
            "data/access.json")
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
        .map(new GaodeLocationMapFunction())
        .map(new MapFunction<Access, Tuple3<String, Integer, Integer>>() {
          @Override
          public Tuple3<String, Integer, Integer> map(Access value) throws Exception {
            return Tuple3.of(value.province, value.nu, 1);
          }
        })
        .keyBy(new KeySelector<Tuple3<String, Integer, Integer>, Tuple2<String, Integer>>() {
          @Override
          public Tuple2<String, Integer> getKey(Tuple3<String, Integer, Integer> value)
              throws Exception {
            return Tuple2.of(value.f0, value.f1);
          }
        })
        .sum(2)
        .print();
    env.execute("OsUserCntAppV1");
  }

}
