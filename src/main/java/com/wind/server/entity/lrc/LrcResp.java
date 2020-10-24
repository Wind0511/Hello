package com.wind.server.entity.lrc;

public class LrcResp {
    Lrc lrc;//原版歌词
    Lrc tlyric;//翻译歌词

    public Lrc getLrc() {
        return lrc;
    }

    public void setLrc(Lrc lrc) {
        this.lrc = lrc;
    }

    public Lrc getTlyric() {
        return tlyric;
    }

    public void setTlyric(Lrc tlyric) {
        this.tlyric = tlyric;
    }
}
