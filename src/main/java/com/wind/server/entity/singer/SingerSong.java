package com.wind.server.entity.singer;

import java.util.List;

public class SingerSong {
    SingerInfo artist;
    List<Song> hotSongs;

    public SingerInfo getArtist() {
        return artist;
    }

    public void setArtist(SingerInfo artist) {
        this.artist = artist;
    }

    public List<Song> getHotSongs() {
        return hotSongs;
    }

    public void setHotSongs(List<Song> hotSongs) {
        this.hotSongs = hotSongs;
    }
}
