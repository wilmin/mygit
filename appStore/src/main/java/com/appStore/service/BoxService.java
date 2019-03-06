package com.appStore.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.appStore.entity.Box;

public interface BoxService {
	int deleteByPrimaryKey(String mac);

    int insert(Box record);

    int insertSelective(Box record);

    Box selectByPrimaryKey(String mac);

    int updateByPrimaryKeySelective(Box record);

    int updateByPrimaryKey(Box record);
    /***
     * *OTA接口访问服务器，通过sn序列号修改访问记录
     * @param record
     * @return
     */
    int updateByOTAPrimaryKeySelective(Box record);
    /**
	 * 查看盒子是否注册
	 * @param mac
	 * @param sn
	 * @return
	 */
    Box selectMacSn(@Param("mac")String mac,@Param("sn")String sn);
    /**
     * *查询访问盒子信息
     * @param map
     * @param pageBounds
     * @return
     */
	List<Box> selectBoxWithFilter();
	/**
	 * <!-- 定时删除一个月前的所有数据 -->
	 * @return
	 */
	int deleteByOneMonth();
	
}
