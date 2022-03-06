package com.imooc.flink.transformation;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.configuration.Configuration;

/**
 * The type Pk map function.
 *
 * @author Galaxy
 * @since 2022 /3/6 23:19
 */
@Slf4j
public class PkMapFunction extends RichMapFunction<String, Access> {

  @Override
  public RuntimeContext getRuntimeContext() {
    log.error("====getRuntimeContext====");
    return super.getRuntimeContext();
  }

  @Override
  public void open(Configuration parameters) throws Exception {
    log.error("====open====");
    super.open(parameters);
  }

  @Override
  public void close() throws Exception {
    log.error("====close====");
    super.close();
  }

  @Override
  public Access map(String value) throws Exception {
    log.error("====map====");
    String[] splits = value.split(",");
    return new Access(Long.parseLong(splits[0].trim()), splits[1],
        Double.parseDouble(splits[2].trim()));
  }
}
