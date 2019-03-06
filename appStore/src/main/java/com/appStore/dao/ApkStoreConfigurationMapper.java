package com.appStore.dao;

import java.util.List;

import com.appStore.entity.ApkStoreConfiguration;

public interface ApkStoreConfigurationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ApkStoreConfiguration record);

    int insertSelective(ApkStoreConfiguration record);

    ApkStoreConfiguration selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ApkStoreConfiguration record);

    int updateByPrimaryKey(ApkStoreConfiguration record);
    
    /**
     * <!-- 查询商城APP升级集合-->
     * @param id
     * @return
     */
    List<ApkStoreConfiguration> selectByApkStoreList();	
    /**
     * <!-- 查询状态通过的商城配置策略信息 -->
     * @param id
     * @return
     */
    ApkStoreConfiguration selectByStoreStatus(Integer id);
    
}