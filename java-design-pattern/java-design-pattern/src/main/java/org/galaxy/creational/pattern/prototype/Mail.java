package org.galaxy.creational.pattern.prototype;

/**
 * @author Galaxy
 * @since 2022/5/3 22:52
 */
public class Mail implements Cloneable {

  private String name;

  private String address;

  private String content;

  public Mail() {
    System.out.println("Mail Class Constructor");
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public String toString() {
    return "Mail{" +
        "name='" + name + '\'' +
        ", address='" + address + '\'' +
        ", content='" + content + '\'' +
        '}';
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
