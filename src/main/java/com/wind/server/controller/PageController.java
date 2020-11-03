package com.wind.server.controller;

import com.wind.server.tools.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class PageController {

    @Autowired
    RedisUtil redisUtil;

    @RequestMapping("admin")
    public String admin() {
        return "admin";
    }

    @RequestMapping("test")
    public String test(HttpSession session, String pass) {
        if (redisUtil.get(session.getId()).equals(pass)) {
            return "test";
        }
        return "<h1>您不是管理员</h1>";
    }
    @RequestMapping("t99")
    public String test2() {

            return "test";

    }

}
