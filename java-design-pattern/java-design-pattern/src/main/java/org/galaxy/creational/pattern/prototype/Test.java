package org.galaxy.creational.pattern.prototype;

/**
 * @author Galaxy
 * @since 2022/5/3 22:55
 */
public class Test {

  public static void main(String[] args) throws CloneNotSupportedException {
    Mail mail = new Mail();
    mail.setContent("初始化模板");

    for (int i = 0; i < 10; i++) {
      Mail tempMail = (Mail) mail.clone();
      tempMail.setName("姓名" + i);
      tempMail.setAddress("姓名" + i + "@imooc.com");
      tempMail.setContent("恭喜您，此次慕课网活动中奖了");
      MailUtil.sendMail(tempMail);
    }
    MailUtil.saveOriginMailRecord(mail);
  }

}
