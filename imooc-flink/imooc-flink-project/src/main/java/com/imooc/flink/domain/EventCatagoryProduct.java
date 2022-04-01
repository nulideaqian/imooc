package com.imooc.flink.domain;

/**
 * @author Galaxy
 * @since 2022/4/2 0:22
 */
public class EventCatagoryProduct {

  public String event;

  public String category;

  public String product;

  public long count;

  public long start;

  public long end;

  public EventCatagoryProduct(String event, String category, String product, long count, long start,
      long end) {
    this.event = event;
    this.category = category;
    this.product = product;
    this.count = count;
    this.start = start;
    this.end = end;
  }

  @Override
  public String toString() {
    return "EventCatagoryProduct{" +
        "event='" + event + '\'' +
        ", category='" + category + '\'' +
        ", product='" + product + '\'' +
        ", count=" + count +
        ", start=" + start +
        ", end=" + end +
        '}';
  }
}
