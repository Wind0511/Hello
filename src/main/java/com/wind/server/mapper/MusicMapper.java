package com.wind.server.mapper;

import com.wind.server.entity.AdminOperation;
import com.wind.server.entity.ErrorCollection;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MusicMapper {
    int Operations(AdminOperation adminOperation);
    int ErrorCollection(ErrorCollection errorCollection);
}
