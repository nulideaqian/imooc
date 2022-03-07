package com.imooc.flink.source;

import com.imooc.flink.transformation.Access;
import java.util.Random;
import org.apache.flink.streaming.api.functions.source.ParallelSourceFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

/**
 * @author Galaxy
 * @since 2022/3/7 0:01
 */
public class AccessSourceV2 implements ParallelSourceFunction<Access> {

  boolean running = true;

  @Override
  public void run(SourceContext<Access> ctx) throws Exception {
    String[] domains = {"imooc.com", "a.com", "b.com"};
    Random random = new Random();
    while (running) {
      for (int i = 0; i < 10; i++) {
        ctx.collect(new Access(1234567L, domains[random.nextInt(domains.length)],
            random.nextDouble() + 1000));
      }
      Thread.sleep(3000L);
    }
  }

  @Override
  public void cancel() {
    running = false;
  }
}
