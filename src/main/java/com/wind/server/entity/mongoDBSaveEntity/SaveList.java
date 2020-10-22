package com.wind.server.entity.mongoDBSaveEntity;

import com.wind.server.entity.search.SearchInfo;

import java.util.List;

public class SaveList {
    String id;
    List<SearchInfo> searchInfos;//统一形式的歌曲存储结构表
    String name;//歌单名称
    String pass;//编辑密码md5加密已经写好

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public List<SearchInfo> getSearchInfos() {
        return searchInfos;
    }

    public void setSearchInfos(List<SearchInfo> searchInfos) {
        this.searchInfos = searchInfos;
    }
}
