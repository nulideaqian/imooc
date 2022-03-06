package com.imooc.flink.transformation;

import java.util.ArrayList;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @author Galaxy
 * @since 2022/3/3 0:21
 */
public class TransformationApp {

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    // map(env);
    // filter(env);
    // flatMap(env);
    //  keyBy(env);
    // reduce(env);
    richMap(env);
    env.execute("Source App");
  }

  private static void richMap(StreamExecutionEnvironment env) {
    env.setParallelism(2);
    DataStreamSource<String> source = env.readTextFile("data/access.log");
    SingleOutputStreamOperator<Access> mapStream = source.map(new PkMapFunction());
    mapStream.keyBy(new KeySelector<Access, String>() {
      @Override
      public String getKey(Access value) throws Exception {
        return value.getDomain();
      }
    }).sum("traffic").print();
  }

  private static void reduce(StreamExecutionEnvironment env) {
    DataStreamSource<String> source = env.socketTextStream("localhost", 9527);
    source.flatMap(new FlatMapFunction<String, String>() {
      @Override
      public void flatMap(String value, Collector<String> out) throws Exception {
        String[] splits = value.split(",");
        for (String split : splits) {
          out.collect(split);
        }
      }
    }).map(new MapFunction<String, Tuple2<String, Integer>>() {
      @Override
      public Tuple2<String, Integer> map(String value) throws Exception {
        return new Tuple2<>(value, 1);
      }
    }).keyBy(new KeySelector<Tuple2<String, Integer>, String>() {
      @Override
      public String getKey(Tuple2<String, Integer> value) throws Exception {
        return value.f0;
      }
    }).reduce(new ReduceFunction<Tuple2<String, Integer>>() {
      @Override
      public Tuple2<String, Integer> reduce(Tuple2<String, Integer> value1,
          Tuple2<String, Integer> value2) throws Exception {
        return Tuple2.of(value1.f0, value1.f1 + value2.f1);
      }
    }).print();
  }

  /**
   * 按照domain分组，求和
   *
   * @param env
   */
  private static void keyBy(StreamExecutionEnvironment env) {
    DataStreamSource<String> source = env.readTextFile("data/access.log");
    SingleOutputStreamOperator<Access> mapStream = source.map(new MapFunction<String, Access>() {
      @Override
      public Access map(String value) throws Exception {
        String[] splits = value.split(",");
        return new Access(Long.parseLong(splits[0].trim()), splits[1],
            Double.parseDouble(splits[2].trim()));
      }
    });
    // mapStream.keyBy("domain").sum("traffic").print();
    mapStream.keyBy(new KeySelector<Access, String>() {
      @Override
      public String getKey(Access value) throws Exception {
        return value.getDomain();
      }
    }).sum("traffic").print();
  }

  private static void flatMap(StreamExecutionEnvironment env) {
    DataStreamSource<String> source = env.socketTextStream("localhost", 9527);
    source.flatMap(new FlatMapFunction<String, String>() {
      @Override
      public void flatMap(String value, Collector<String> out) throws Exception {
        String[] splits = value.split(",");
        for (String split : splits) {
          out.collect(split);
        }
      }
    }).print();
  }

  private static void filter(StreamExecutionEnvironment env) {
    DataStreamSource<String> source = env.readTextFile("data/access.log");
    SingleOutputStreamOperator<Access> mapStream = source.map(new MapFunction<String, Access>() {
      @Override
      public Access map(String value) throws Exception {
        String[] splits = value.split(",");
        return new Access(Long.parseLong(splits[0].trim()), splits[1],
            Double.parseDouble(splits[2].trim()));
      }
    });
    SingleOutputStreamOperator<Access> filterStream = mapStream.filter(
        new FilterFunction<Access>() {
          @Override
          public boolean filter(Access value) throws Exception {
            return value.getTraffic() > 4000;
          }
        });
    filterStream.print();
  }

  private static void map(StreamExecutionEnvironment env) {
    ArrayList<Integer> list = new ArrayList<>();
    list.add(1);
    list.add(2);
    list.add(3);
    DataStreamSource<Integer> source = env.fromCollection(list);
    source.map(new MapFunction<Integer, Integer>() {
      @Override
      public Integer map(Integer value) throws Exception {
        return value * 2;
      }
    }).print();
  }

}
