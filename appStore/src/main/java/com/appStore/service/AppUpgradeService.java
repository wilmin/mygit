package com.appStore.service;

import java.util.List;

import com.appStore.entity.AppUpgrade;

public interface AppUpgradeService {
	int deleteByPrimaryKey(Integer id);

    int insert(AppUpgrade record);

    int insertSelective(AppUpgrade record);

    AppUpgrade selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppUpgrade record);

    int updateByPrimaryKey(AppUpgrade record);
    
    /***
     * *查询所有的APP升级推送信息
     * @return
     */
    List<AppUpgrade> getAppUpgradeAll();
    
    /**
     * <!-- APP商城推送查询APP集合 -->
     * <!-- 根据机型编号查询关联商城配置表，拿到商城配置表apkid，再拿apkid到APP升级表编号查询apkid结合列 -->
     * @param id
     * @return
     */
    AppUpgrade selectByStoreApkIdList(Integer id);
    /**
     * <!-- 商城默认推送APP安装列表 -->
     * @param android
     * @return
     */
    List<AppUpgrade> selectByDefaultStoreApkList(String android);
    /**
     * <!-- APP商城配置列表 --> 商城列表下拉框选项
     * @return
     */
    List<AppUpgrade> selectByStoreApkList();
    
    /**
     * <!-- 自动、静默配置APP下拉框选项 -->
     * @return
     */
    List<AppUpgrade> selectByAutoStaticApkList();
    
    /**
     * *通过名称获取升级对象集合
     * @param name
     * @return
     */
    AppUpgrade selectByNameApkList(AppUpgrade record);
 
}
