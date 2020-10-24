package com.wind.server.dao;


import com.wind.server.mapper.MusicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginDao {
    @Autowired
    MusicMapper musicMapper;
    public String Test(int id){
        String relpass= null;
        return musicMapper.getSinger(id);
    }

//    public Boolean check(String name) {
//        if (loginMapper.Check(name) == 0) {
//            return true;
//        }
//        return false;
//    }
//    public int insert(User user){
//        return loginMapper.insertUser(user);
//    }

}
