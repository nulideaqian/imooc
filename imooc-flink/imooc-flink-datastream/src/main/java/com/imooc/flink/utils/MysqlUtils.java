package com.imooc.flink.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Galaxy
 * @since 2022/3/8 1:58
 */
public class MysqlUtils {

  public static Connection getConnection() {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      return DriverManager.getConnection("jdbc:mysql://localhost:3306/pk_flink_imooc", "root", "root");
    } catch (ClassNotFoundException | SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static void close(Connection connection, PreparedStatement preparedStatement) {
    if (connection != null) {
      try {
        connection.close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
    if (preparedStatement != null) {
      try {
        preparedStatement.close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
  }

}
