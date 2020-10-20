package com.wind.server.entity.list;

import com.wind.server.entity.search.Songs;

import java.util.List;

public class ListResult {
    String name;
    int id;
    String coverImgUrl;
    List<Songs> tracks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public List<Songs> getTracks() {
        return tracks;
    }

    public void setTracks(List<Songs> tracks) {
        this.tracks = tracks;
    }
}
