package com.wind.server.controller;

import com.alibaba.fastjson.JSON;
import com.wind.server.dao.MainDao;
import com.wind.server.entity.mongoSave.SaveList;
import com.wind.server.entity.search.SearchInfo;
import com.wind.server.tools.IpUtils;
import com.wind.server.tools.Md5String;
import com.wind.server.tools.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    IpUtils ipUtils;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    MainDao mainDao;

    Md5String md5String =
            new Md5String();

    @ResponseBody
    @RequestMapping("geTime")
    public String getime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        return sdf.format(cal.getTime());
    }

    //Submit JSON 必须用POST否则报错
    //mongoDB存储歌单 打包成SaveList形式的JSON然后转到后端 其中id不填
    @ResponseBody
    @RequestMapping("save")
    public String save(String json,HttpServletRequest request) {
//        System.out.println(json);
        Md5String md5String = new Md5String();
//        SaveList s = JSON.parseObject(json, SaveList.class);
//        s.setPass(md5String.getMd5(s.getPass()));
//        s.setId(UUID.randomUUID().toString());
//        s.setTime(System.currentTimeMillis());
//        mongoTemplate.insert(s, "music");
//        return "success";
        SaveList saveList = new SaveList();
        saveList.setPass(md5String.getMd5(json));
        HttpSession session = request.getSession();
        List<SearchInfo> searchInfos = (JSON.parseArray((String) session.getAttribute("songs"),SearchInfo.class));
        saveList.setName((String) session.getAttribute("name"));
        saveList.setInform((String) session.getAttribute("inform"));
        saveList.setId(UUID.randomUUID().toString());
        saveList.setSearchInfos(searchInfos);
        saveList.setTime(System.currentTimeMillis());try {
            mongoTemplate.insert(saveList, "music");
            return "true";
        }catch (Exception e){
            mainDao.errorCollection(request.getRequestURI(), "saveError" + e.toString());
            return "error";
        }
    }

    //删除歌单 返回值是删除记录的个数 如果是0就是密码错误如果不是0那就是删除成功 需要歌单名和密码
    @ResponseBody
    @RequestMapping("del")
    public String del(String id, String pass, HttpServletRequest request) {
        HttpSession session = request.getSession();
        try {
            if (redisUtil.get(session.getId()).equals(pass)) {
                Md5String md5String = new Md5String();
                Query query = new Query(Criteria.where("_id").is(id));
                return mongoTemplate.remove(query, "music").getDeletedCount()+"";
            } else {
                Md5String md5String = new Md5String();
                List<Criteria> criteria = new ArrayList<>();
                criteria.add(Criteria.where("_id").is(id));
                criteria.add(Criteria.where("pass").is(pass));
                Query query = new Query(new Criteria().andOperator(criteria.toArray(new Criteria[]{})));
                return mongoTemplate.remove(query, "music").getDeletedCount()+"";
            }

        } catch (Exception e) {
            mainDao.errorCollection(request.getRequestURI(), "你不是管理员,或密码错误 " + e.toString());
            return "你不是管理员,或密码错误";
        }

    }

    //编辑后更改歌单 返回值如果是0就是密码错误如果不是0那就是更改成功  需要歌单名，密码，歌单SearchInfo形式的JSON
    @ResponseBody
    @RequestMapping("edit")
    public long edit(String name, String pass, String json) {
        Md5String md5String = new Md5String();
        List<Criteria> criteria = new ArrayList<>();
        criteria.add(Criteria.where("name").is(name));
        criteria.add(Criteria.where("pass").is(pass));
        Query query = new Query(new Criteria().andOperator(criteria.toArray(new Criteria[]{})));
        Update update = new Update().set("searchInfos", JSON.parseObject(json, SearchInfo.class));
        return mongoTemplate.updateFirst(query, update, "music").getModifiedCount();
    }

    //用id精确确定一个歌单
    @RequestMapping("get")
    @ResponseBody
    public String get(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        SaveList saveList = mongoTemplate.findOne(query, SaveList.class, "music");
        saveList.setPass("");
        saveList.setInform("");
        return JSON.toJSONString(saveList);
    }

    //用名字查找歌单
    @RequestMapping("get2")
    @ResponseBody
    public String get2(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        List<SaveList> saveList = mongoTemplate.find(query, SaveList.class, "music");
        for (int i = 0; i < saveList.size(); i++) {
            saveList.get(i).setPass("");
            saveList.get(i).setInform("");
        }
        return JSON.toJSONString(saveList);
    }

    //拿到所有歌单列表
    @ResponseBody
    @RequestMapping("get3")
    public String get3() {
        Query query = new Query();
        List<SaveList> saveList = mongoTemplate.find(query, SaveList.class, "music");
        for (int i = 0; i < saveList.size(); i++) {
            saveList.get(i).setPass("");
            saveList.get(i).setInform("");
        }
        return JSON.toJSONString(saveList);
    }

    //***************************************************************************************************管理员
    @ResponseBody
    @RequestMapping("get4")
    public String get4(HttpSession session, String pass, HttpServletRequest request) {
        try {
            if (redisUtil.get(session.getId()).equals(pass)) {
                Query query = new Query();
                List<SaveList> saveList = mongoTemplate.find(query, SaveList.class, "music");
                for (int i = 0; i < saveList.size(); i++) {
                    saveList.get(i).setPass("");
                }
                mainDao.adminOperationInformation(session, request, request.getRequestURI() + ":" + request.getMethod());
                return JSON.toJSONString(saveList);
            } else
                return "您不是管理员";
        } catch (Exception e) {
            mainDao.errorCollection(request.getRequestURI(), "你不是管理员,或出现其他异常 " + e.toString());
            return "你不是管理员";
        }
    }

    //inform:预留信息 id：歌单id adminpass：管理员密码 userpass：用户密码
    @RequestMapping("pwdutil")
    @ResponseBody
    public String pwdUtil(HttpSession session, HttpServletRequest request, String inform, String id, String adminpass, String userpass) {
        try {
            if (redisUtil.get(session.getId()).equals(adminpass)) {
                List<Criteria> criteria = new ArrayList<>();
                criteria.add(Criteria.where("inform").is(inform));
                criteria.add(Criteria.where("_id").is(id));
                Query query = new Query(new Criteria().andOperator(criteria.toArray(new Criteria[]{})));
                SaveList saveList = mongoTemplate.findOne(query, SaveList.class, "music");
                if (saveList == null) {
                    return "预留信息不正确";
                }
                Md5String md5String = new Md5String();
                Update update = new Update().set("pass", md5String.getMd5(userpass));
                mongoTemplate.updateFirst(query, update, "music").getModifiedCount();
                mainDao.adminOperationInformation(session, request, request.getRequestURI() + ":" + request.getMethod());
                return "SUCCESS";

            } else
                return "您不是管理员";
        } catch (Exception e) {
            mainDao.errorCollection(request.getRequestURI(), "你不是管理员,或出现其他异常 " + e.toString());
            return "你不是管理员，或出现其他异常 " + e.toString();
        }
    }

    //int 歌单id
    @ResponseBody
    @RequestMapping("get5")
    public String get4(HttpSession session, String pass, int id, HttpServletRequest request) {
        try {
            if (redisUtil.get(session.getId()).equals(pass)) {
                Query query = new Query(Criteria.where("id").is(id));
                mongoTemplate.remove(query);
                mainDao.adminOperationInformation(session, request, request.getRequestURI() + ":" + request.getMethod());
                return "success";
            } else
                return "您不是管理员";
        } catch (Exception e) {
            mainDao.errorCollection(request.getRequestURI(), "你不是管理员,或出现其他异常 " + e.toString());
            return "你不是管理员,或出现其他异常 " + e.toString();
        }
    }
}
