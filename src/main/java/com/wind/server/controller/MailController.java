package com.wind.server.controller;

import com.wind.server.dao.MainDao;
import com.wind.server.entity.AdminOperation;
import com.wind.server.service.EmailService;
import com.wind.server.tools.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class MailController {
    @Autowired
    MainDao mainDao;
    @Autowired
    IpUtils ipUtils;
    @Autowired
    EmailService emailService;
    @ResponseBody
    @RequestMapping("mail")
    public String mail(HttpSession session, HttpServletRequest request){


        Boolean b
         = emailService.sendHtmlEmail(session.getId());
        mainDao.adminOperationInformation(session,request,request.getRequestURI()+":"+request.getMethod());
        return b.toString();
    }
    @ResponseBody
    @RequestMapping("check")
    public String check(HttpSession session,String code, HttpServletRequest request){

        mainDao.adminOperationInformation(session,request,request.getRequestURI()+":"+request.getMethod());
        Boolean b
                = emailService.checkMail(session.getId(),code);
        System.err.println(b);
        return b.toString();
    }

}
