package com.wind.server.controller;

import com.alibaba.fastjson.JSON;
import com.wind.server.entity.mongoDBSaveEntity.SaveList;
import com.wind.server.service.EmailService;
import com.wind.server.tools.Md5String;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.UUID;
@Controller
public class MailController {

    @Autowired
    EmailService emailService;
    @ResponseBody
    @RequestMapping("mail")
    public String mail(HttpSession session){

        Boolean b
         = emailService.sendHtmlEmail(session.getId());
        return b.toString();
    }
    @ResponseBody
    @RequestMapping("check")
    public String check(HttpSession session,String code){

        Boolean b
                = emailService.checkMail(session.getId(),code);
        System.err.println(b);
        return b.toString();
    }

}
