package com.wind.server.dao;


import com.wind.server.entity.User;
import com.wind.server.mapper.LoginMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginDao {
    @Autowired
    LoginMapper loginMapper ;
    public Boolean aBoolean(String name,String pass){
        String relpass= null;
        relpass = loginMapper.pass(name);
        if (pass.equals(relpass)){return true;}return false;
    }

    public Boolean check(String name) {
        if (loginMapper.Check(name) == 0) {
            return true;
        }
        return false;
    }
    public int insert(User user){
        return loginMapper.insertUser(user);
    }

}
