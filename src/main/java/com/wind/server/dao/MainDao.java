package com.wind.server.dao;


import com.wind.server.entity.AdminOperation;
import com.wind.server.entity.ErrorCollection;
import com.wind.server.mapper.MusicMapper;
import com.wind.server.tools.IpUtils;
import com.wind.server.tools.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Service
public class MainDao {
    /**
     *
     */
    @Autowired
    MusicMapper musicMapper;

    public int adminOperationInformation(HttpSession session, HttpServletRequest request,String type){
        IpUtils ipUtils = new IpUtils();
        AdminOperation adminOperation = new AdminOperation(type,ipUtils.getIpAddr(request),new TimeUtil().getTime(),session.getId());
        return musicMapper.Operations(adminOperation);
    }
    public int errorCollection(String request,String error){
        ErrorCollection errorCollection = new ErrorCollection(new TimeUtil().getTime(),request,error);
        return musicMapper.ErrorCollection(errorCollection);
    }


}
