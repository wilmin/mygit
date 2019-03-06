package com.appStore.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.appStore.entity.Appstore;

public interface AppstoreMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(Appstore record);

    int insertSelective(Appstore record);

    Appstore selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Appstore record);

    int updateByPrimaryKeyWithBLOBs(Appstore record);

    int updateByPrimaryKey(Appstore record);
    
    //以下为自定义方法
    /***
     ** 终端商店访问apk信息
     * @return
     */
    List<Appstore> getByAppstoreAll();
    
    /****
     * *下载apk信息次数累计
     * @param id
     * @return
     */
    Integer updateForDownloadTotal(Integer id);
    
	Integer selectlastid();
	
	/**
	 * web前端显示APP集合信息
	 * @return
	 */
	List<Appstore> selectAllAppstoreInfoWithFilter();
	
	
	/**
	 * <!-- 检测是否已上传相同的apk -->
	 * @return
	 */
	Appstore selectByApkInfo(Appstore record);

}