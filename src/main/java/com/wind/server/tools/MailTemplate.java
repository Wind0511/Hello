package com.wind.server.tools;

public class MailTemplate {
    public String getEmailHtml(String session,String code){
        String html = "<div style=\"padding: 0px 0px 0px 20px;box-sizing: border-box;color: #333333;font-family: \"microsoft yahei\";font-size: 14px\">" +
                "<h3 style=\"font-weight: normal;font-size: 18px;\">验证码</h3>" +
                "<h4 style=\"color:#2672EC;font-size: 40px;margin-top: 24px;font-weight: normal;\">账号注册验证码</h4>" +
                "<div style=\"margin-top: 40px;\">您好，您想要获取管理员权限,sessionID为<a href=\"javascript:;\" target=\"_blank\" style=\"color: #2672EC;text-decoration: none;\">"+session+"</a></div>" +
                "<div style=\"margin-top: 30px;\">您的注册验证码为：<em style=\"font-style: normal;font-weight: 600;\">"+code+"</em></div>" +
                "<div style=\"margin-top: 35px;\">谢谢！</div>" +
                "</div>";
        return html;
    }
}
