package com.appStore.dao;

import java.util.List;

import com.appStore.entity.ApkStaticConfiguration;

public interface ApkStaticConfigurationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ApkStaticConfiguration record);

    int insertSelective(ApkStaticConfiguration record);

    ApkStaticConfiguration selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ApkStaticConfiguration record);

    int updateByPrimaryKey(ApkStaticConfiguration record);
    /**
     * <!-- 查询静默安装推送的APP升级集合-->
     * @param id
     * @return
     */
    List<ApkStaticConfiguration> selectByApkStaticList();	
    /**
     * <!-- 查询状态通过的静默安装策略信息 -->
     * @param id
     * @return
     */
    ApkStaticConfiguration selectByApkStaticStatus(Integer id);
}