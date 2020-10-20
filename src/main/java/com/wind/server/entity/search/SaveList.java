package com.wind.server.entity.search;

import java.util.List;

public class SaveList {
    List<SearchInfo> searchInfos;
    String name;
    String pass;

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
