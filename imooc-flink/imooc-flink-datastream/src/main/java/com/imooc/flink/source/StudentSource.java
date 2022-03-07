package com.imooc.flink.source;

import com.imooc.flink.utils.MysqlUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

/**
 * @author Galaxy
 * @since 2022/3/8 1:14
 */
public class StudentSource extends RichSourceFunction<Student> {

  Connection connection;

  PreparedStatement preparedStatement;

  @Override
  public void open(Configuration parameters) throws Exception {
    connection = MysqlUtils.getConnection();
    preparedStatement = connection.prepareStatement("SELECT * FROM student");
  }

  @Override
  public void run(SourceContext<Student> ctx) throws Exception {
    ResultSet resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
      int id = resultSet.getInt("id");
      String name = resultSet.getString("name");
      int age = resultSet.getInt("age");

      ctx.collect(new Student(id, name, age));
    }
  }

  @Override
  public void close() throws Exception {
    MysqlUtils.close(connection, preparedStatement);
  }

  @Override
  public void cancel() {

  }
}
