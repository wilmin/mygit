package com.appStore.service;

import java.util.List;

import com.appStore.entity.ApkAutomaticConfiguration;

public interface ApkAutoConfigurationService {
	int deleteByPrimaryKey(Integer id);

    int insert(ApkAutomaticConfiguration record);

    int insertSelective(ApkAutomaticConfiguration record);

    ApkAutomaticConfiguration selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ApkAutomaticConfiguration record);

    int updateByPrimaryKey(ApkAutomaticConfiguration record);
    
    /**
     * <!-- 查询自动配置推送的APP升级集合-->
     * @param id
     * @return
     */
    List<ApkAutomaticConfiguration> selectByApkAutoList();	
    /**
     * <!-- 查询状态通过的自动配置策略信息 -->
     * @param id
     * @return
     */
    ApkAutomaticConfiguration selectByApkAutoStatus(Integer id);
}
