package com.imooc.flink.app;

import com.alibaba.fastjson.JSON;
import com.imooc.flink.app.OsUserCntAppV1.RedisExampleMapper;
import com.imooc.flink.domain.Access;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;

/**
 * @author Galaxy
 * @since 2022/3/13 12:45
 */
@Slf4j
public class OsUserCntAppV2 {

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   * @throws Exception the exception
   */
  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

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
        .map(new MapFunction<Access, Tuple2<Integer, Integer>>() {
          @Override
          public Tuple2<Integer, Integer> map(Access value) throws Exception {
            return Tuple2.of(value.nu, 1);
          }
        })
        .keyBy(new KeySelector<Tuple2<Integer, Integer>, Integer>() {
          @Override
          public Integer getKey(Tuple2<Integer, Integer> value)
              throws Exception {
            return value.f0;
          }
        })
        .sum(1)
        .print()
        .setParallelism(1);

    env.execute("OsUserCntAppV1");
  }
}
