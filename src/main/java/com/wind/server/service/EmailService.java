package com.wind.server.service;

import com.sun.mail.util.MailSSLSocketFactory;
import com.wind.server.dao.MainDao;
import com.wind.server.tools.MailTemplate;
import com.wind.server.tools.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class EmailService {


    @Autowired
    MainDao mainDao;

    @Autowired
    RedisUtil redisUtil;

    @Value("${spring.mail.username}")
    public String from;
    @Value("${spring.mail.password}")
    public String password;// 登录密码
    @Value("${spring.mail.protocol}")
    public String protocol;// 协议
    @Value("${spring.mail.port}")
    public String port;// 端口
    @Value("${spring.mail.host}")
    public String host;// 服务器地址

    private String theme = "验证码";


    //初始化参数
    public Session initProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", protocol);
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", port);
        // 使用smtp身份验证
        properties.put("mail.smtp.auth", "true");
        // 使用SSL,企业邮箱必需 start
        // 开启安全协议
        MailSSLSocketFactory mailSSLSocketFactory = null;
        try {
            mailSSLSocketFactory = new MailSSLSocketFactory();
            mailSSLSocketFactory.setTrustAllHosts(true);
        } catch (GeneralSecurityException e) {
            mainDao.errorCollection("EmailService initProperties", e.toString());
            e.printStackTrace();
        }
        properties.put("mail.smtp.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", mailSSLSocketFactory);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", "false");
        properties.put("mail.smtp.socketFactory.port", port);
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
        // 使用SSL,企业邮箱必需 end
        return session;
    }

    //发送
    public Boolean sendHtmlEmail(String sessionID) {
        boolean lean = false;
        try {
            Session session = initProperties();
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(from, "Wind0511"));// 发件人,可以设置发件人的别名
            // 收件人
            mimeMessage.setRecipients(Message.RecipientType.TO, "857093701@qq.com");
            // 主题
            mimeMessage.setSubject(theme);
            // 时间
            mimeMessage.setSentDate(new Date());
            // 容器类 附件
            MimeMultipart mimeMultipart = new MimeMultipart();
            // 可以包装文本,图片,附件
            MimeBodyPart bodyPart = new MimeBodyPart();
            String code = UUID.randomUUID().toString();
            // 设置内容 getEmailHtml是邮箱内容模板
            MailTemplate mailTemplate = new MailTemplate();
            bodyPart.setContent(mailTemplate.getEmailHtml(sessionID, code), "text/html; charset=UTF-8");
            mimeMultipart.addBodyPart(bodyPart);
            // 添加图片&附件
//            bodyPart = new MimeBodyPart();
//            bodyPart.attachFile(fileSrc);
//            mimeMultipart.addBodyPart(bodyPart);
            mimeMessage.setContent(mimeMultipart);
            mimeMessage.saveChanges();
            Transport.send(mimeMessage);
            redisUtil.set(sessionID, code);
            lean = true;
        } catch (MessagingException e) {
            mainDao.errorCollection("EmailService MessagingException sendHtmlEmail", e.toString());
            e.printStackTrace();
            lean = false;
        } catch (UnsupportedEncodingException e) {
            mainDao.errorCollection("EmailService UnsupportedEncodingException sendHtmlEmail", e.toString());
            e.printStackTrace();
            lean = false;
        }
        return lean;
    }

    public Boolean checkMail(String sessionID, String pass) {
        if (pass.equals(redisUtil.get(sessionID))) {
            return true;
        }
        return false;
    }
}
