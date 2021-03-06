package com.imooc.flink.transformation;

import com.imooc.flink.source.AccessSource;
import com.imooc.flink.source.AccessSourceV2;
import java.util.ArrayList;
import java.util.Locale;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoFlatMapFunction;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;
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
    // richMap(env);
    // union(env);
    // connect(env);
    // coMap(env);
    coFlatMap(env);
    env.execute("Source App");
  }

  private static void coFlatMap(StreamExecutionEnvironment env) {
    DataStreamSource<String> source1 = env.fromElements("a b c", "d e f");
    DataStreamSource<String> source2 = env.fromElements("1,2,3", "4,5,6");
    ConnectedStreams<String, String> connect = source1.connect(source2);
    connect.flatMap(new CoFlatMapFunction<String, String, String>() {
      @Override
      public void flatMap1(String value, Collector<String> out) throws Exception {
        for (String item : value.split(" ")) {
          out.collect(item);
        }
      }

      @Override
      public void flatMap2(String value, Collector<String> out) throws Exception {
        for (String item : value.split(",")) {
          out.collect(item);
        }
      }
    }).print();
  }

  private static void coMap(StreamExecutionEnvironment env) {
    DataStreamSource<String> source1 = env.socketTextStream("localhost", 9527);
    SingleOutputStreamOperator<Integer> source2 = env.socketTextStream("localhost", 9528)
        .map(new MapFunction<String, Integer>() {
          @Override
          public Integer map(String value) throws Exception {
            return Integer.parseInt(value);
          }
        });
    ConnectedStreams<String, Integer> connect = source1.connect(source2);
    connect.map(new CoMapFunction<String, Integer, String>() {
      @Override
      public String map1(String value) throws Exception {
        return value.toUpperCase(Locale.ROOT);
      }

      @Override
      public String map2(Integer value) throws Exception {
        return value * 10 + "";
      }
    }).print();
  }

  private static void connect(StreamExecutionEnvironment env) {
    DataStreamSource<Access> source1 = env.addSource(new AccessSource());
    DataStreamSource<Access> source2 = env.addSource(new AccessSource());
    SingleOutputStreamOperator<Tuple2<String, Access>> source2New = source2.map(
        new MapFunction<Access, Tuple2<String, Access>>() {
          @Override
          public Tuple2<String, Access> map(Access value) throws Exception {
            return Tuple2.of("pk", value);
          }
        });
    // ConnectedStreams<Access, Access> connect = source1.connect(source2);
    // connect.map(new CoMapFunction<Access, Access, Access>() {
    //   @Override
    //   public Access map1(Access value) throws Exception {
    //     return value;
    //   }
    //
    //   @Override
    //   public Access map2(Access value) throws Exception {
    //     return value;
    //   }
    // }).print();
    source1.connect(source2New).map(new CoMapFunction<Access, Tuple2<String, Access>, String>() {
      @Override
      public String map1(Access value) throws Exception {
        return value.toString();
      }

      @Override
      public String map2(Tuple2<String, Access> value) throws Exception {
        return value.f0 + " ---> " + value.f1.toString();
      }
    }).print();
  }

  private static void union(StreamExecutionEnvironment env) {
    DataStreamSource<String> source1 = env.socketTextStream("localhost", 9527);
    DataStreamSource<String> source2 = env.socketTextStream("localhost", 9528);
    source1.union(source2).print();
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
   * ??????domain???????????????
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
