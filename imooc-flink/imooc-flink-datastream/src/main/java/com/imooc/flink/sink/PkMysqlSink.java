package com.imooc.flink.sink;

import com.imooc.flink.transformation.Access;
import com.imooc.flink.utils.MysqlUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

/**
 * @author Galaxy
 * @since 2022/3/9 1:27
 */
public class PkMysqlSink extends RichSinkFunction<Tuple2<String, Double>> {

  private Connection connection;

  private PreparedStatement preparedPstmt;

  private PreparedStatement updatePstmt;

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);

    connection = MysqlUtils.getConnection();
    preparedPstmt = connection.prepareStatement("INSERT INTO pk_traffic (domain, traffic) values (?, ?)");
    updatePstmt = connection.prepareStatement(
        "update pk_traffic SET traffic = ? WHERE domain = ?");
  }

  @Override
  public void close() throws Exception {
    super.close();
    if (preparedPstmt != null) {
      MysqlUtils.close(connection, preparedPstmt);
    }
  }

  @Override
  public void invoke(Tuple2<String, Double> value, Context context) throws Exception {
    super.invoke(value, context);
    System.out.println("----invoke----" + value.f0 + " ---> " + value.f1);

    updatePstmt.setDouble(1, value.f1);
    updatePstmt.setString(2, value.f0);
    updatePstmt.execute();
  }
}
