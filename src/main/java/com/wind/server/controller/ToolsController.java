package com.wind.server.controller;

import com.wind.server.dao.LoginDao;
import com.wind.server.tools.Md5String;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Controller
public class ToolsController {


    @Autowired
    LoginDao loginDao;
    Md5String md5String = new Md5String();

    @ResponseBody
    @RequestMapping("geTime")
    public String getime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        return sdf.format(cal.getTime());
    }


}
