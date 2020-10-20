package com.wind.server.entity.singer;

public class Song {
    String name;
    int id;
    SingerAlbum album;

    public SingerAlbum getAlbum() {
        return album;
    }

    public void setAlbum(SingerAlbum album) {
        this.album = album;
    }

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
}
