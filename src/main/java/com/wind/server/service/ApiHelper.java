package com.wind.server.service;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import com.alibaba.fastjson.JSONObject;
import com.wind.server.dao.MainDao;
import com.wind.server.entity.list.*;
import com.wind.server.entity.lrc.LrcResp;
import com.wind.server.entity.search.SearchInfo;
import com.wind.server.entity.search.Select;
import com.wind.server.entity.search.SelectAlbum;
import com.wind.server.entity.search.Songs;
import com.wind.server.entity.singer.SingerSong;
import com.wind.server.entity.singer.Song;
import com.wind.server.tools.URLEncodTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiHelper {
    @Autowired
    MainDao mainDao;

    //资料爬取************************************************************************************
    //拿取歌词
    public String lrc(int id) throws IOException {
        URL u = new URL("http://music.163.com/api/song/lyric?os=pc&id=" + id + "&lv=-1&kv=-1&tv=-1");
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

        return JSON.toJSONString(JSON.parseObject(Json, LrcResp.class));
    }

    //搜索服务
    public String Searcher(String name, int num) throws IOException {
        URLEncodTools urlEncodTools = new URLEncodTools();
        String enc = urlEncodTools.encode(name);
        System.out.println(enc);
        URL u = new URL("https://music.163.com/api/search/get?s=" + enc + "&type=1&limit=" + num);
        //获取连接对象
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        //连接
        try {


            conn.connect();
        } catch (Exception e) {
            mainDao.errorCollection("ApiHelper Search", e.toString());
            return null;
        }
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

    //搜索服务(歌单)
    public String Searcher2(String name, int num) throws IOException {
        URLEncodTools urlEncodTools = new URLEncodTools();
        String enc = urlEncodTools.encode(name);
        System.out.println(enc);
        URL u = new URL("https://music.163.com/api/search/get?s=" + enc + "&type=1000&limit=" + num);
        //获取连接对象
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        //连接
        try {
            conn.connect();
        } catch (Exception e) {
            mainDao.errorCollection("ApiHelper search2", e.toString());
            return null;
        }
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
        return JSONObject.toJSONString(JSON.parseObject(Json, ListSearchPackage.class).getResult());
    }

    //得到专辑图片
    public String getPic(int id) throws IOException {

//        URL u = new URL("https://music.163.com/api/song/detail/?id=" + id + "&ids=[" + id + "]");
        URL u = new URL("http://music.163.com/api/album/"+id);
        System.err.println(u.toString());
        //获取连接对象
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        //连接
        try {

            conn.connect();
        } catch (Exception e) {
            mainDao.errorCollection("ApiHelper getPic", e.toString());
            return null;
        }
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
        try {


            conn.connect();
        } catch (Exception e) {
            mainDao.errorCollection("ApiHelper getAlbum", e.toString());
            return null;
        }
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
        try {
            conn.connect();
        } catch (Exception e) {
            mainDao.errorCollection("ApiHelper singer", e.toString());
            return null;
        }
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

    //拿取歌单
    public ListResult list(long id) throws IOException {

        URL u = new URL("https://music.163.com/api/playlist/detail?id=" + id);
        //获取连接对象
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        //连接
        try {
            conn.connect();
        } catch (Exception e) {
            mainDao.errorCollection("ApiHelper list(long id)", e.toString());
            return null;
        }
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
        ListResult listResult = JSON.parseObject(Json, ListPack.class).getResult();
        return listResult;
    }
    //                     ___                _---ヘ
//                    く__,.ヘヽ.　　　　  /　,ー､ 〉
//            　　　　   　＼ ', !-─‐-i　/　
//            　　　 　    ／｀ｰ'　　　 L/／｀ヽ､
//            　　   　 /　 ／,　 /|　 ,　 ,　  ',
//            　　   　ｲ 　/ /-‐/　ｉ　L_ ﾊ ヽ!　 i
//    　　　             ﾚ ﾍ 7ｲ｀ﾄ　 ﾚ'ｧ-ﾄ､!ハ|　 |
//            　　 　  !,/  '0'　　 ´0'  ソ| 　  |　　　
//            　　　　 |.从"　　  v      / |./ 　|
//            　　　　 ﾚ'| i＞.､,,__　_,.イ / .i　|
//            　　　　　 ﾚ'| | / k_７_/ﾚ'ヽ,　ﾊ.　|
//            　　　　　　| |/i 〈|/　 i　,.ﾍ |　i　|
//            　　　　　　|/ /　ｉ： 　 ﾍ!　　＼　|
//            　　　 　  kヽ>､ﾊ 　 _,.ﾍ､ 　 /､!
//            　　　　　 !'〈//｀Ｔ´', ＼ ｀'7'ｰr'
//            　　　　　 ﾚ'ヽL__|___i,___,ンﾚ|ノ
//            　　　　　 　  　ﾄ-,/　|___./
//            　　　　　 　  　'ｰ'　  !_,.:                powered by wind0511
//
    //Json解析区******************************************************************************8
    //得到歌手图片
    public String getSingerPic(int id) {
        SingerSong singerSong = null;
        try {
            System.out.println("*******" + singerSong);
            singerSong = singer(id);
            System.out.println("*******" + singerSong);
        } catch (IOException e) {

            mainDao.errorCollection("ApiHelper getStringPic", e.toString());
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
            List<Song> songs = singerSong.getHotSongs();
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
            return JSONObject.toJSONString(searchInfos);
        } catch (Exception e) {
            mainDao.errorCollection("ApiHelper getSingerSong", e.toString());
            return null;
        }
    }

    //打包歌单 搜索歌曲结果
    public String editRes(String Json) {
        try {

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
        } catch (Exception e) {
            mainDao.errorCollection("ApiHelper editRes", e.toString());
            return null;
        }
    }

    //打包专辑列表
    public String editAlbum(String Json) {
        try {
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
        } catch (Exception e) {
            mainDao.errorCollection("ApiHelper editAlbum", e.toString());
            return null;
        }
    }

    //拿取专辑图片URL
    public String editInfo(String Json) {
        try {


            Json = "{\"result\":" + Json + "}";
            Select select = JSON.parseObject(Json, Select.class);
System.err.println(Json);
            return select.getResult().getAlbum().getPicURL();
        } catch (Exception e) {
            mainDao.errorCollection("ApiHelper editInfo", e.toString());
            return null;
        }
    }

    //歌单拿取
    public String list(ListResult listResult) {
        try {

            List<Songs> songs = listResult.getTracks();
            System.err.println(JSON.toJSONString(listResult));
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
        } catch (Exception e) {
            mainDao.errorCollection("ApiHelper list", e.toString());
            return null;
        }
    }

    //歌单信息获取
    public String listInfo(ListResult listResult) {
        try {
            ListInfo listInfo = new ListInfo();
            listInfo.setId(listResult.getId());
            listInfo.setName(listResult.getName());
            listInfo.setUrl(listResult.getCoverImgUrl());
            return JSON.toJSONString(listInfo);
        } catch (Exception e) {
            mainDao.errorCollection("ApiHelper listInfo", e.toString());
            return null;
        }
    }
}
