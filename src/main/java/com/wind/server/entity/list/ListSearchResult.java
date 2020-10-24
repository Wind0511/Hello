package com.wind.server.entity.list;

import java.util.List;

public class ListSearchResult {
    List<ListSearchInfo> playlists;

    public List<ListSearchInfo> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<ListSearchInfo> playlists) {
        this.playlists = playlists;
    }
}
