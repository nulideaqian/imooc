package com.imooc.flink.partitioner;

import com.imooc.flink.source.AccessSource;
import com.imooc.flink.source.Student;
import com.imooc.flink.source.StudentSource;
import com.imooc.flink.transformation.Access;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author Galaxy
 * @since 2022/3/9 0:43
 */
public class PartitionerApp {

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(3);
    DataStreamSource<Access> source = env.addSource(new AccessSource());
    source.map(new MapFunction<Access, Tuple2<String, Access>>() {
          @Override
          public Tuple2<String, Access> map(Access value) throws Exception {
            return Tuple2.of(value.getDomain(), value);
          }
        }).partitionCustom(new PkPartitioner(), new KeySelector<Tuple2<String, Access>, String>() {
          @Override
          public String getKey(Tuple2<String, Access> value) throws Exception {
            return value.f1.getDomain();
          }
        })
        .map(new MapFunction<Tuple2<String, Access>, Access>() {
          @Override
          public Access map(Tuple2<String, Access> value) throws Exception {
            // System.out.println("curr thread id is: " + Thread.currentThread().getId() + ", value is: " + value.f1.toString());
            return value.f1;
          }
        }).print();
    env.execute("partitioner app");
  }

}
