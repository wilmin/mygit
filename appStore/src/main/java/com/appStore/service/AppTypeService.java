package com.appStore.service;

import java.util.List;

import com.appStore.entity.AppType;

public interface AppTypeService {
	int deleteByPrimaryKey(Integer id);

    int insert(AppType record);

    int insertSelective(AppType record);

    AppType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppType record);

    int updateByPrimaryKey(AppType record);
    
    List<AppType> selectByApkTypeAll();
}
