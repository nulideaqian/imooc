package com.imooc.flink.source;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.NumberSequenceIterator;

/**
 * @author Galaxy
 * @since 2022/2/24 1:16
 */
@Slf4j
public class SourceApp {

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    // test01(env);
    // test03(env);
    test04(env);
    env.execute("sourceApp");
  }

  private static void test04(StreamExecutionEnvironment env) {

  }

  private static void test03(StreamExecutionEnvironment env) {
    KafkaSource<String> source = KafkaSource.<String>builder()
        .setBootstrapServers("localhost:9092")
        .setTopics("flinktopic")
        .setGroupId("goahead")
        .setStartingOffsets(OffsetsInitializer.latest())
        .setValueOnlyDeserializer(new SimpleStringSchema())
        .build();
    DataStreamSource<String> stream = env.fromSource(source, WatermarkStrategy.noWatermarks(),
        "kafka source");

//    Properties properties = new Properties();
//    properties.setProperty("bootstrap.servers", "localhost:9092");
//    properties.setProperty("group.id", "test");
//    DataStream<String> stream = env.addSource(
//        new FlinkKafkaConsumer<String>("flinktopic", new SimpleStringSchema(), properties));
    log.info("{}", stream.getParallelism());
    stream.print();
  }

  private static void test02(StreamExecutionEnvironment env) {
    DataStreamSource<Long> source = env.fromParallelCollection(
        new NumberSequenceIterator(1, 10), Long.class);
    log.info("source: {}", source.getParallelism());

    SingleOutputStreamOperator<Long> filter = source.filter(new FilterFunction<Long>() {
      @Override
      public boolean filter(Long value) throws Exception {
        return value > 4;
      }
    });
    filter.print();
    log.info("filter: {}", filter.getParallelism());
  }

  private static void test01(StreamExecutionEnvironment env) {
    DataStreamSource<String> source = env.socketTextStream("localhost", 9527, ",");
    log.info("source: {}", source.getParallelism());

    SingleOutputStreamOperator<String> filterStream = source.filter(new FilterFunction<String>() {
      @Override
      public boolean filter(String value) throws Exception {
        return !"pk".equals(value);
      }
    }).setParallelism(4);
    log.info("filter: {}", filterStream.getParallelism());
  }

}
