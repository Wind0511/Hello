package com.wind.server.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MusicMapper {
    String getSinger(int id);

}
