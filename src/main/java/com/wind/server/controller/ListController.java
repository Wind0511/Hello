package com.wind.server.controller;

import com.alibaba.fastjson.JSON;
import com.wind.server.dao.LoginDao;
import com.wind.server.entity.mongoDBSaveEntity.SaveList;
import com.wind.server.entity.search.SearchInfo;
import com.wind.server.tools.Md5String;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Controller
public class ListController {
    @Autowired
    MongoTemplate mongoTemplate;

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
    //Submit JSON 必须用POST否则报错
    //mongoDB存储歌单 打包成SaveList形式的JSON然后转到后端 其中id不填
    @ResponseBody
    @RequestMapping("save")
    public String  save(String json)
    {
        System.out.println(json);
        Md5String md5String = new Md5String();
        SaveList s = JSON.parseObject(json,SaveList.class);
        s.setPass(md5String.getMd5(s.getPass()));
        s.setId(UUID.randomUUID().toString());
        mongoTemplate.insert(s,"music");
        return "success";
    }
    //删除歌单 返回值是删除记录的个数 如果是0就是密码错误如果不是0那就是删除成功 需要歌单名和密码
    @ResponseBody
    @RequestMapping("del")
    public long del(String name,String pass){
        Md5String md5String = new Md5String();
        List<Criteria> criteria = new ArrayList<>();
        criteria.add(Criteria.where("name").is(name));
        criteria.add(Criteria.where("pass").is(pass));
        Query query = new Query(new Criteria().andOperator(criteria.toArray(new Criteria[]{})));
        return mongoTemplate.remove(query,"music").getDeletedCount();
    }
    //编辑后更改歌单 返回值如果是0就是密码错误如果不是0那就是更改成功  需要歌单名，密码，歌单SearchInfo形式的JSON
    @ResponseBody
    @RequestMapping("edit")
    public long edit(String name,String pass,String json) {
        Md5String md5String = new Md5String();
        List<Criteria> criteria = new ArrayList<>();
        criteria.add(Criteria.where("name").is(name));
        criteria.add(Criteria.where("pass").is(pass));
        Query query = new Query(new Criteria().andOperator(criteria.toArray(new Criteria[]{})));
        Update update = new Update().set("searchInfos",JSON.parseObject(json, SearchInfo.class));
        return mongoTemplate.updateFirst(query,update,"music").getModifiedCount();
    }
    //用id精确确定一个歌单
    @RequestMapping("get")
    @ResponseBody
    public String get(String id){
        Query query = new Query(Criteria.where("id").is(id));
        SaveList saveList = mongoTemplate.findOne(query,SaveList.class,"music");
        saveList.setPass("");
        return JSON.toJSONString(saveList);
    }
    //用名字模糊查找一个歌单
    @RequestMapping("get2")
    @ResponseBody
    public String get2(String name){
        Query query = new Query(Criteria.where("name").is(name));
        List<SaveList> saveList = mongoTemplate.find(query,SaveList.class,"music");
        for (int i = 0;i<saveList.size();i++){
            saveList.get(i).setPass("");
        }
        return JSON.toJSONString(saveList);
    }
    //拿到所有歌单列表
    @ResponseBody
    @RequestMapping("get3")
    public String get2(){
        Query query = new Query();
        List<SaveList> saveList = mongoTemplate.find(query,SaveList.class,"music");
        for (int i = 0;i<saveList.size();i++){
            saveList.get(i).setPass("");
        }
        return JSON.toJSONString(saveList);
    }

    }
