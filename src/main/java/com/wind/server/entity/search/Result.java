package com.wind.server.entity.search;

import java.util.List;

public class Result {
    List<Songs> songs;

    Album album;

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public List<Songs> getSongs() {
        return songs;
    }

    public void setSongs(List<Songs> songs) {
        this.songs = songs;
    }
}
