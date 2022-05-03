package org.galaxy.creational.pattern.prototype;

import java.text.MessageFormat;

/**
 * @author Galaxy
 * @since 2022/5/3 22:53
 */
public class MailUtil {

  public static void sendMail(Mail mail) {
    String outputContent = "向{0}同学，邮件地址{1}，邮件内容: {2}，发送成功";
    System.out.println(
        MessageFormat.format(outputContent, mail.getName(), mail.getAddress(), mail.getContent()));
  }

  public static void saveOriginMailRecord(Mail mail) {
    System.out.println("存储originMail记录，originMail：" + mail.getContent());
  }

}
