package com.wind.server.entity.search;

import java.util.List;

public class Songs {
    int id;
    String name;
    List<Artists> artists;
    Album album;
    int mvid;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Artists> getArtists() {
        return artists;
    }

    public void setArtists(List<Artists> artists) {
        this.artists = artists;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public int getMvid() {
        return mvid;
    }

    public void setMvid(int mvid) {
        this.mvid = mvid;
    }
}
