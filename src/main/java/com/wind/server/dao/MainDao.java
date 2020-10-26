package com.wind.server.dao;


import com.wind.server.entity.AdminOperation;
import com.wind.server.mapper.MusicMapper;
import com.wind.server.tools.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Service
public class MainDao {
    @Autowired
    MusicMapper musicMapper;

    public int adminOperationInformation(HttpSession session, HttpServletRequest request,String type){
        IpUtils ipUtils = new IpUtils();
        AdminOperation adminOperation = new AdminOperation();
        adminOperation.setSession(session.getId());
        adminOperation.setIp(ipUtils.getIpAddr(request));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        adminOperation.setTime(sdf.format(cal.getTime()).toString());
        adminOperation.setType(type);
        return musicMapper.Operations(adminOperation);
    }
    public int errorCollection(HttpServletRequest request){
        return 1;
    }


}
