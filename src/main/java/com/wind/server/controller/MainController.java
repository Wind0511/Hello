package com.wind.server.controller;

import com.alibaba.fastjson.JSON;
import com.wind.server.dao.MainDao;
import com.wind.server.entity.mongoSave.SaveList;
import com.wind.server.entity.search.SearchInfo;
import com.wind.server.entity.singer.SingerSong;
import com.wind.server.entity.singer.Song;
import com.wind.server.service.ApiHelper;
import com.wind.server.tools.Md5String;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Controller
public class MainController {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    MainDao mainDao;

    @Autowired
    ApiHelper search;


    public String list(int id) {
        return "";
    }
//这个是获取歌词
    @ResponseBody
    @RequestMapping("lrc")
    public String lrc(int id, HttpServletRequest request) {
        try {
            return search.lrc(id);
        } catch (IOException e) {
            mainDao.errorCollection(request.getRequestURI(),e.toString());
            e.printStackTrace();
        }
        return "ERROR";
    }

    //传入搜索内容 和 搜索条数（歌手专辑歌曲名均可）默认返回搜索内容前50 Json SearchInfo List
    @ResponseBody
    @RequestMapping("search")
    public String search(String name, int num,HttpServletRequest request) {
        if ("0".equals(String.valueOf(num)) || "null".equals(String.valueOf(num)) || num <= 0) {
            num = 50;
        }
        try {
            return search.Searcher(name,num);
        } catch (IOException e) {
            mainDao.errorCollection(request.getRequestURI(),e.toString());
            e.printStackTrace();
        }
        return "ERROR";
    }
    //搜索歌单
    @ResponseBody
    @RequestMapping("search2")
    public String search2(String name, int num,HttpServletRequest request) {
        if ("0".equals(String.valueOf(num)) || "null".equals(String.valueOf(num)) || num <= 0) {
            num = 50;
        }
        try {
            return search.Searcher2(name,num);
        } catch (IOException e) {
            mainDao.errorCollection(request.getRequestURI(),e.toString());
            e.printStackTrace();
        }
        return "ERROR";
    }

    //这个是获取播放地址
    @RequestMapping("song")
    @ResponseBody
    public String songURL(int id,HttpServletRequest request) {

        return "http://music.163.com/song/media/outer/url?id=" + id + ".mp3";
    }
    //这个是获取图片
    @RequestMapping("sp")
    @ResponseBody
    public String songPic(int id,HttpServletRequest request) {
        String pic = null;
        try {
            pic = search.getPic(id);
        } catch (IOException e) {
            mainDao.errorCollection(request.getRequestURI(),e.toString());
            e.printStackTrace();
        }
        return pic;
    }

    //传入专辑id返回专辑歌曲
    @ResponseBody
    @RequestMapping("album")
    public String getAlbum(int id,HttpServletRequest request) {
        try {
            return search.getAlbum(id);
        } catch (IOException e) {
            mainDao.errorCollection(request.getRequestURI(),e.toString());
            e.printStackTrace();
        }
        return "ERROR";
    }

    //传入歌手id返回歌手图片 String URL
    @ResponseBody
    @RequestMapping("singerpic" )
    public String getSingerPic(int id,HttpServletRequest request) {
        return search.getSingerPic(id);
    }

    //传入歌手id返回播放量前50 Json  SearchInfo List
    @ResponseBody
    @RequestMapping("singersong")
    public String getSingerSong(int id) {
        return search.getSingerSong(id);
    }

    //list id拿取歌单 Json  SearchInfo List
    @ResponseBody
    @RequestMapping("getlist")
    public String getList(String id,HttpServletRequest request) {
        long i = Long.parseLong(id);
        try {
            return search.list(search.list(i));
        } catch (IOException e) {
            mainDao.errorCollection(request.getRequestURI(),e.toString());
            e.printStackTrace();
        }
        return "ERROR";
    }

    //list id拿取歌单信息 Json  ListInfo Object
    @ResponseBody
    @RequestMapping("getlistInfo")
    public String getListInfo(String id,HttpServletRequest request) {
        long i = Long.parseLong(id);
        try {
            return search.listInfo(search.list(i));
        } catch (IOException e) {

            mainDao.errorCollection(request.getRequestURI(),e.toString());
            e.printStackTrace();
        }
        return "ERROR";
    }

    //MongoDB_Demo Design by Wind0511*************************************
    //增
    @RequestMapping("p")
    @ResponseBody
    public String testMongo() {
        SingerSong singerSong = null;
        List<Song> songs=null;
        try {
            singerSong = search.singer(2116);
            songs= singerSong.getHotSongs();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<SearchInfo> searchInfos = new ArrayList<>();
        for (int i = 0; i < songs.size(); i++) {
            SearchInfo searchInfo = new SearchInfo();
            searchInfo.setAlbumId(songs.get(i).getAlbum().getId());
            searchInfo.setAlbumName(songs.get(i).getAlbum().getName());
            searchInfo.setSingerId(singerSong.getArtist().getId());
            searchInfo.setSingerName(singerSong.getArtist().getName());
            searchInfo.setSongName(songs.get(i).getName());
            searchInfo.setSongId(songs.get(i).getId());
            searchInfos.add(searchInfo);
        }

        SaveList saveList = new SaveList();
        saveList.setId(UUID.randomUUID().toString());
        saveList.setSearchInfos(searchInfos);
        saveList.setName("狗子77");
        Md5String md5String = new Md5String();
        saveList.setPass(md5String.getMd5("123"));
        mongoTemplate.insert(saveList, "music");
        return "ok";
    }

    //删
    @RequestMapping("d")
    @ResponseBody
    public String testMongo2() {
        Query query = new Query(Criteria.where("name").is("狗子"));
        mongoTemplate.remove(query, "music");
        return "";
    }

    //改
    @RequestMapping("m")
    @ResponseBody
    public String testMongo3() {
        Query query = new Query(Criteria.where("name").is("狗子"));
        Md5String md5String = new Md5String();
        Update update = new Update().set("pass", md5String.getMd5("2333"));
        mongoTemplate.updateFirst(query, update, "music");
        return "";
    }

    //查
    @RequestMapping("s")
    @ResponseBody
    public String testMongo4() {
        Query query = new Query(Criteria.where("name").is("狗子"));
        List<SaveList> saveLists = mongoTemplate.find(query, SaveList.class, "music");
        System.out.println(saveLists.size());
        return JSON.toJSONString(saveLists);
    }
    //MongoDB_Demo Design by Wind0511*************************************

}
