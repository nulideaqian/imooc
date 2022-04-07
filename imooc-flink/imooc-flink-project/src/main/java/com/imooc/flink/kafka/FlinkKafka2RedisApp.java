package com.imooc.flink.kafka;

import com.imooc.flink.utils.FlinkUtils;
import java.io.IOException;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;
import org.apache.flink.util.Collector;

/**
 * @author Galaxy
 * @since 2022/4/8 2:32
 */
public class FlinkKafka2RedisApp {

  public static void main(String[] args) throws Exception {
    ParameterTool tool = ParameterTool.fromPropertiesFile(
        "E:\\code\\github\\imooc\\imooc-flink\\imooc-flink-project\\src\\main\\resources\\pk.properties");
    DataStream<String> stream = FlinkUtils.createKafkaStreamV1(tool);
    FlinkJedisPoolConfig conf = new FlinkJedisPoolConfig.Builder().setHost("127.0.0.1").build();
    stream.flatMap(new FlatMapFunction<String, String>() {
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
        .addSink(new RedisSink<>(conf, new RedisExampleMapper()));

    FlinkUtils.env.execute();
  }

  public static class RedisExampleMapper implements RedisMapper<Tuple2<String, Integer>> {

    @Override
    public RedisCommandDescription getCommandDescription() {
      return new RedisCommandDescription(RedisCommand.HSET, "wc");
    }

    @Override
    public String getKeyFromData(Tuple2<String, Integer> data) {
      return data.f0;
    }

    @Override
    public String getValueFromData(Tuple2<String, Integer> data) {
      return String.valueOf(data.f1);
    }
  }

}
