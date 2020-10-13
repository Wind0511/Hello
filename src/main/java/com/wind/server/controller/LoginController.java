package com.wind.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.wind.server.dao.LoginDao;
import com.wind.server.entity.Backup;
import com.wind.server.entity.User;
import com.wind.server.mapper.LoginMapper;
import com.wind.server.tools.md5String;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Controller
public class LoginController {


    @Autowired
    LoginDao loginDao;
    md5String md5String = new md5String();
    final static String FILE_HOME = "D:/File/";
    @RequestMapping("/namecheck")
    @ResponseBody
    public String nameCheck(String name) {
        System.out.println(name);
        if (loginDao.check(name)) {
            //System.out.println("11111");
            return "老铁没毛病";
        }
        //System.out.println("22222");
        return "这名有人起过了奥";
    }
    @ResponseBody
    @PostMapping("/register")
    public String register(@ModelAttribute User user, HttpServletRequest request) {
        user.setPass(md5String.getMd5(user.getPass()));//MD5加密密码
        int a = loginDao.insert(user);
        if (a != 1) {
            return "FAILURE";
        }
        File dir = new File(FILE_HOME);
        if (!dir.exists()){
            dir.mkdirs();
        }
        String Filename = user.getName();//+UUID.randomUUID().toString();
        File file = new File(FILE_HOME+Filename);
        Backup backup = new Backup();
        backup.setInform(null);
        backup.setMoney(0.0);
        backup.setTime(0);
        backup.setType("shipin");
        String JSON = JSONObject.toJSONString(backup);
        System.out.println(JSON);
        FileWriter fwriter = null;
        try {
            fwriter = new FileWriter(FILE_HOME+Filename);
            fwriter.write(JSON);
            fwriter.flush();
            fwriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }finally {
        }
        return "SUCCESS";
    }
    @RequestMapping("/log")
    @ResponseBody
    public String log(String name,String pass){
        System.out.println(name+"   "+pass);
        if(loginDao.aBoolean(name, pass)){
            return "success";
        }
        else
        return "false";
    }
    @ResponseBody
    @RequestMapping("getime")
    public String getime(String name){
        File f = new File(FILE_HOME+name);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(f.lastModified());
        return sdf.format(cal.getTime());
    }
    @ResponseBody
    @RequestMapping("del")
    public String del(String name){
        File f = new File(FILE_HOME+name);
        f.delete();
        return "success";
    }
    @RequestMapping("/update")
    @ResponseBody
    public String upload(MultipartFile pic)  {

        File dir = new File(FILE_HOME);
        if (!dir.exists()){
            dir.mkdirs();
        }
        String name = pic.getOriginalFilename();
        String Filename = name;//+UUID.randomUUID().toString();
        File file = new File(FILE_HOME+Filename);
        try{
            pic.transferTo(file);}
        catch (Exception e){
            e.printStackTrace();
            return "error";
        }
        return "success";
    }
    @RequestMapping("up")
    @ResponseBody
    public String up(){
        return "<html>\n" +
                " \n" +
                "<head>\n" +
                "    <title>练习</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<form action=\"/update\" enctype=\"multipart/form-data\" method=\"post\">\n" +
                "    <input type=\"text\" name=\"username\">\n" +
                "    <input type=\"password\" name=\"pwd\">\n" +
                "    <input type=\"file\" name=\"pic\">\n" +
                "    <input type=\"submit\">\n" +
                "</form>\n" +
                " \n" +
                "</body>\n" +
                "</html>\n" ;
    }

    @RequestMapping("download")
    public String downloadFile(HttpServletRequest request, HttpServletResponse response, String name) throws Exception {


            File file = new File(FILE_HOME+name);                                                                                                                
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + name);// 设置文件名
                response.setContentType("multipart/form-data;charset=UTF-8");//也可以明确的设置一下UTF-8，测试中不设置也可以。
                response.setHeader("Content-Disposition", "attachment;fileName="+ new String(name.getBytes("utf-8"),"utf-8"));
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    System.out.println("下载成功");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        return null;
    }
    @RequestMapping("reg")
    public String reg(){
        return "register";
    }
}
