package com.wind.server.entity.search;

public class SelectAlbum {
    int code;
    Result album;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Result getAlbum() {
        return album;
    }

    public void setAlbum(Result album) {
        this.album = album;
    }
}
