package com.imooc.flink.sink;

import com.imooc.flink.transformation.Access;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;

/**
 * @author Galaxy
 * @since 2022/3/10 0:50
 */
public class SinkApp {

  public static void main(String[] args) throws Exception {
    FlinkJedisPoolConfig conf = new FlinkJedisPoolConfig.Builder().setHost("localhost").build();
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    DataStreamSource<String> source = env.readTextFile("data/access.log");
    source.map(new MapFunction<String, Access>() {
      @Override
      public Access map(String value) throws Exception {
        String[] splits = value.split(",");
        return new Access(Long.parseLong(splits[0].trim()), splits[1],
            Double.parseDouble(splits[2].trim()));
      }
    }).map(new MapFunction<Access, Tuple2<String, Double>>() {
      @Override
      public Tuple2<String, Double> map(Access value) throws Exception {
        return Tuple2.of(value.getDomain(), value.getTraffic());
      }
    }).addSink(new RedisSink<>(conf, new PkRedisSink()));
    env.execute("sink app");
  }

}
