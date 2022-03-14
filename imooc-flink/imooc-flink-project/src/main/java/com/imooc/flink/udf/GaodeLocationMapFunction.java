package com.imooc.flink.udf;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imooc.flink.domain.Access;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;

/**
 * @author Galaxy
 * @since 2022/3/13 18:39
 */
public class GaodeLocationMapFunction extends RichMapFunction<Access, Access> {

  @Override
  public void open(Configuration parameters) throws Exception {
  }

  @Override
  public void close() throws Exception {
  }

  @Override
  public Access map(Access value) throws Exception {
    String url = "https://restapi.amap.com/v3/ip?ip=" + value.ip
        + "&output=json&key=e7df2d542ccb4547b3a2d6be172bdd55";
    String respBody = HttpUtil.get(url);
    value.province = JSON.parseObject(respBody, JSONObject.class).getString("province");
    return value;
  }
}
