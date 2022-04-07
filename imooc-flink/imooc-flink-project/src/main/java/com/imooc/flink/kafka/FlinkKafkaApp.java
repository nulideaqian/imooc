package com.imooc.flink.kafka;

import com.imooc.flink.utils.FlinkUtils;
import java.util.concurrent.TimeUnit;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig.ExternalizedCheckpointCleanup;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author Galaxy
 * @since 2022/4/4 11:47
 */
public class FlinkKafkaApp {

  public static void main(String[] args) throws Exception {
    ParameterTool tool = ParameterTool.fromPropertiesFile(args[0]);
    DataStream<String> stream = FlinkUtils.createKafkaStreamV1(tool);
    stream
        .print();
    // .flatMap(new FlatMapFunction<String, String>() {
        //   @Override
        //   public void flatMap(String value, Collector<String> out) throws Exception {
        //     String[] words = value.split(",");
        //     for (String word : words) {
        //       out.collect(word);
        //     }
        //   }
        // })
        // .map(new MapFunction<String, Tuple2<String, Long>>() {
        //   @Override
        //   public Tuple2<String, Long> map(String value) throws Exception {
        //     return Tuple2.of(value, 1L);
        //   }
        // })
        // .keyBy(x -> x.f0)
        // .sum(1)
        // .print();

    FlinkUtils.env.execute();
  }

  private static void test01() throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
        .setBootstrapServers("localhost:9092")
        .setGroupId("test")
        .setTopics("pk10")
        .setProperty("enable.auto.commit", "false")
        .setStartingOffsets(OffsetsInitializer.earliest())
        .setValueOnlyDeserializer(new SimpleStringSchema())
        .build();

    env.enableCheckpointing(5000);
    env.setStateBackend(new FsStateBackend(""));
    env.getCheckpointConfig().enableExternalizedCheckpoints(ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
    env.setRestartStrategy(RestartStrategies.fixedDelayRestart(2, Time.of(5, TimeUnit.SECONDS)));

    env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "kafka source")
        .setParallelism(1)
        .print();
    env.execute();
  }

}
