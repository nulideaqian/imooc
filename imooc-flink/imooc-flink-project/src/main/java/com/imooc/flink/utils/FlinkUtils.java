package com.imooc.flink.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.runtime.state.hashmap.HashMapStateBackend;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * @author Galaxy
 * @since 2022/4/4 17:43
 */
public class FlinkUtils {

  public static StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

  public static <T> DataStream<T> createKafkaStreamV2(ParameterTool tool,
      Class<? extends KafkaRecordDeserializationSchema<T>> deserializationSchema)
      throws InstantiationException, IllegalAccessException {
    String groupId = tool.get("group.id", "test");
    String servers = tool.getRequired("bootstrap.servers");
    List<String> topics = Arrays.asList(tool.getRequired("kafka.input.topics").split(","));
    String autoCommit = tool.get("enable.auto.commit", "false");

    KafkaSource<T> kafkaSource = KafkaSource.<T>builder()
        .setGroupId(groupId)
        .setBootstrapServers(servers)
        .setTopics(topics)
        .setProperty("enable.auto.commit", autoCommit)
        .setStartingOffsets(OffsetsInitializer.earliest())
        .setDeserializer(deserializationSchema.newInstance())
        .build();

    int checkpointInterval = tool.getInt("checkpoint.interval", 5000);
    String checkpointPath = tool.get("checkpoint.path", "file:///e/flink-test/");

    env.enableCheckpointing(checkpointInterval);
    env.setStateBackend(new HashMapStateBackend());
    env.getCheckpointConfig().setCheckpointStorage(checkpointPath);

    return env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "kafka source")
        .setParallelism(1);
  }

  public static DataStream<String> createKafkaStreamV1(ParameterTool tool) {
    String groupId = tool.get("group.id", "test");
    String servers = tool.getRequired("bootstrap.servers");
    List<String> topics = Arrays.asList(tool.getRequired("kafka.input.topics").split(","));
    String autoCommit = tool.get("enable.auto.commit", "false");

    KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
        .setGroupId(groupId)
        .setBootstrapServers(servers)
        .setTopics(topics)
        .setProperty("enable.auto.commit", autoCommit)
        .setStartingOffsets(OffsetsInitializer.earliest())
        .setDeserializer(KafkaRecordDeserializationSchema.valueOnly(StringDeserializer.class))
        .build();

    int checkpointInterval = tool.getInt("checkpoint.interval", 5000);
    String checkpointPath = tool.get("checkpoint.path", "file:///e/flink-test/");

    env.enableCheckpointing(checkpointInterval);
    env.setStateBackend(new HashMapStateBackend());
    env.getCheckpointConfig().setCheckpointStorage(checkpointPath);

    return env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "kafka source")
        .setParallelism(1);
  }

  public static void main(String[] args) throws IOException {
    ParameterTool tool = ParameterTool.fromPropertiesFile(args[0]);
    String groupId = tool.get("group.id", "test");
    String servers = tool.getRequired("bootstrap.servers");

    System.out.println(groupId);
    System.out.println(servers);
  }

}
