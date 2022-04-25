package org.galaxy.principle.openclose;

/**
 * @author Galaxy
 * @since 2022/4/25 23:47
 */
public class JavaDiscountCourse extends JavaCourse {

  public JavaDiscountCourse(Integer id, String name, Double price) {
    super(id, name, price);
  }

  public Double getOriginPrice() {
    return super.getPrice();
  }

  @Override
  public Double getPrice() {
    return super.getPrice() * 0.8;
  }
}
