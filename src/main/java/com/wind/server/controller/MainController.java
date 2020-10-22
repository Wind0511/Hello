package com.wind.server.controller;

import com.alibaba.fastjson.JSON;
import com.wind.server.dao.LoginDao;
import com.wind.server.entity.search.SaveList;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Controller
public class MainController {
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    LoginDao loginDao;
    @Autowired
    ApiHelper search;
    @RequestMapping("test")
    public String reg(){
        return "register";
    }
    @RequestMapping("tdb")
    @ResponseBody
    public String a(){
        return  loginDao.Test(1);
    }
    public  String list(int id){
        return "";
    }
    //传入搜索内容（歌手专辑歌曲名均可）返回搜索内容前50 Json  SearchInfo List
    @ResponseBody
    @RequestMapping("search")
    public String search(String name){
        try {
            return search.Searcher(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }
    //传入歌曲id返回播放地址URL Json  SearchInfo List
    @RequestMapping("song")
    @ResponseBody
    public String songURL(int id){
        String pic = null;
        try {
            pic = search.getPic(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "{\"song\":\"http://music.163.com/song/media/outer/url?id="+id+".mp3\",\"pic\":\""+pic+"\"}";
    }
    //传入专辑id返回专辑歌曲
    @ResponseBody
    @RequestMapping("album")
    public String getAlbum(int id){
        try {
            return search.getAlbum(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }
    //传入歌手id返回歌手图片 String URL
    @ResponseBody
    @RequestMapping("singerpic")
    public String getSingerPic(int id){
        return search.getSingerPic(id);
    }
    //传入歌手id返回播放量前50 Json  SearchInfo List
    @ResponseBody
    @RequestMapping("singersong")
    public String getSingerSong(int id){
        return search.getSingerSong(id);
    }
    //增
    @RequestMapping("p")
    @ResponseBody
    public String testMongo(){
        SingerSong singerSong = null;
        try {
            singerSong = search.singer(2116);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Song> songs = singerSong.getHotSongs();
        List<SearchInfo> searchInfos = new ArrayList<>();
        for(int i=0;i<songs.size();i++){
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
        saveList.setSearchInfos(searchInfos);
        saveList.setName("狗子");
        Md5String md5String = new Md5String();
        saveList.setPass(md5String.getMd5(System.currentTimeMillis()+""));
        mongoTemplate.insert(saveList,"music");
        return "ok";
    }
    //删
    @RequestMapping("d")
    @ResponseBody
    public String testMongo2(){
        Query query = new Query(Criteria.where("name").is("狗子"));
        mongoTemplate.remove(query,"music");
        return "";
    }
    //改
    @RequestMapping("m")
    @ResponseBody
    public String testMongo3(){
        Query query = new Query(Criteria.where("name").is("狗子"));
        Md5String md5String = new Md5String();
        Update update = new Update().set("pass",md5String.getMd5("2333"));
        mongoTemplate.updateFirst(query,update,"music");
        return "";
    }
    //查
    @RequestMapping("s")
    @ResponseBody
    public String testMongo4(){
        Query query = new Query(Criteria.where("name").is("狗子"));
        List<SaveList> saveLists = mongoTemplate.find(query,SaveList.class,"music");
        System.out.println(saveLists.size());
        return JSON.toJSONString(saveLists);
    }
}