package com.wind.server.mapper;

import com.wind.server.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper {
    String pass(String name);
    int insertUser(User user);
    int Check(String name);
}
