package com.wind.server.service;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import com.alibaba.fastjson.JSONObject;
import com.wind.server.entity.search.SearchInfo;
import com.wind.server.entity.search.Select;
import com.wind.server.entity.search.SelectAlbum;
import com.wind.server.entity.search.Songs;
import com.wind.server.entity.singer.SingerSong;
import com.wind.server.entity.singer.Song;
import com.wind.server.tools.URLEncodTools;
import org.springframework.stereotype.Service;

@Service
public class ApiHelper {
    //搜索服务
    public String Searcher(String name) throws IOException {
        URLEncodTools urlEncodTools = new URLEncodTools();
        String enc = urlEncodTools.encode(name);
        System.out.println(enc);
        URL u = new URL("https://music.163.com/api/search/get?s=" + enc + "&type=1&limit=100");
        //获取连接对象
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        //连接
        conn.connect();
        //获取输入流shinian
        InputStream in = conn.getInputStream();
        //读取输入流
        int r;
        byte[] bs = new byte[1024];
        StringBuffer sb = new StringBuffer();
        while ((r = in.read(bs)) != -1) {
            sb.append(new String(bs, 0, r));
        }
        in.close();
        String Json = sb.toString();

        return editRes(Json);
    }
    //得到专辑图片
    public String getPic(int id) throws IOException {

        URL u = new URL("https://music.163.com/api/song/detail/?id=" + id + "&ids=[" + id + "]");
        //获取连接对象
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        //连接
        conn.connect();
        //获取输入流shinian
        InputStream in = conn.getInputStream();
        //读取输入流
        int r;
        byte[] bs = new byte[1024];
        StringBuffer sb = new StringBuffer();
        while ((r = in.read(bs)) != -1) {
            sb.append(new String(bs, 0, r));
        }
        in.close();
        String Json = sb.toString();
        return editInfo(Json);
    }
    //拿到专辑
    public String getAlbum(int id) throws IOException {

        URL u = new URL("http://music.163.com/api/album/" + id + "/");
        //获取连接对象
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        //连接
        conn.connect();
        //获取输入流shinian
        InputStream in = conn.getInputStream();
        //读取输入流
        int r;
        byte[] bs = new byte[1024];
        StringBuffer sb = new StringBuffer();
        while ((r = in.read(bs)) != -1) {
            sb.append(new String(bs, 0, r));
        }
        in.close();
        String Json = sb.toString();
        return editAlbum(Json);
    }

    //拿到歌手热歌前50
    public SingerSong singer(int id) throws IOException {

        URL u = new URL("http://music.163.com/api/artist/" + id);
        //获取连接对象
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        //连接
        conn.connect();
        //获取输入流shinian
        InputStream in = conn.getInputStream();
        //读取输入流
        int r;
        byte[] bs = new byte[1024];
        StringBuffer sb = new StringBuffer();
        while ((r = in.read(bs)) != -1) {
            sb.append(new String(bs, 0, r));
        }
        in.close();
        String Json = sb.toString();

        return JSON.parseObject(Json, SingerSong.class);
    }
    //得到歌手图片
    public String getSingerPic(int id) {
        SingerSong singerSong = null;
        try {
            System.out.println("*******"+singerSong);
            singerSong = singer(id);
            System.out.println("*******"+singerSong);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = singerSong.getArtist().getPicUrl();
        return url;
    }
    //打包歌手前50
    public String getSingerSong(int id) {
        SingerSong singerSong = null;
        try {
            singerSong = singer(id);
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
        return JSONObject.toJSONString(searchInfos);
    }

    //打包歌单 搜索歌曲结果
    public String editRes(String Json) {
        Select select = JSON.parseObject(Json, Select.class);
        List<Songs> songs = select.getResult().getSongs();
        List<SearchInfo> searchInfos = new ArrayList<>();
        for (int i = 0; i < songs.size(); i++) {
            SearchInfo searchInfo = new SearchInfo();
            searchInfo.setAlbumId(songs.get(i).getAlbum().getId());
            searchInfo.setAlbumName(songs.get(i).getAlbum().getName());
            int art = songs.get(i).getArtists().size();
            String arts = "";
            for (int j = 0; j < songs.get(i).getArtists().size(); j++) {
                if (j > 0)
                    arts = arts + " and ";
                arts = arts + songs.get(i).getArtists().get(j).getName();

            }
            searchInfo.setSingerId(songs.get(i).getArtists().get(0).getId());
            searchInfo.setSingerName(arts);
            searchInfo.setSongId(songs.get(i).getId());
            searchInfo.setSongName(songs.get(i).getName());
            searchInfos.add(searchInfo);
        }
        return JSONObject.toJSONString(searchInfos);
    }

    //打包专辑列表
    public String editAlbum(String Json) {
        SelectAlbum selectAlbum = JSON.parseObject(Json, SelectAlbum.class);
        List<Songs> songs = selectAlbum.getAlbum().getSongs();
        List<SearchInfo> searchInfos = new ArrayList<>();
        for (int i = 0; i < songs.size(); i++) {
            SearchInfo searchInfo = new SearchInfo();
            searchInfo.setAlbumId(songs.get(i).getAlbum().getId());
            searchInfo.setAlbumName(songs.get(i).getAlbum().getName());
            int art = songs.get(i).getArtists().size();
            String arts = "";
            for (int j = 0; j < songs.get(i).getArtists().size(); j++) {
                if (j > 0)
                    arts = arts + " and ";
                arts = arts + songs.get(i).getArtists().get(j).getName();

            }
            searchInfo.setSingerId(songs.get(i).getArtists().get(0).getId());
            searchInfo.setSingerName(arts);
            searchInfo.setSongId(songs.get(i).getId());
            searchInfo.setSongName(songs.get(i).getName());
            searchInfos.add(searchInfo);
        }
        return JSONObject.toJSONString(searchInfos);
    }

    //拿取专辑图片URL
    public String editInfo(String Json) {
        Json = "{\"result\":" + Json + "}";
        Select select = JSON.parseObject(Json, Select.class);
        Songs songs = select.getResult().getSongs().get(0);
        return songs.getAlbum().getPicURL();
    }
}
