package com.imooc.flink.source;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Galaxy
 * @since 2022/3/8 1:13
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Student {

  private int id;

  private String name;

  private int age;

}
