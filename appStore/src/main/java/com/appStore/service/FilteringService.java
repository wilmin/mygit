package com.appStore.service;

import java.util.List;
import java.util.Map;

import com.appStore.entity.Filtering;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

public interface FilteringService {
	Integer deleteByPrimaryKey(Integer id);

	Integer insert(Filtering record);

	Integer insertSelective(Filtering record);

    Filtering selectByPrimaryKey(Integer id);

    Integer updateByPrimaryKeySelective(Filtering record);

    Integer updateByPrimaryKey(Filtering record);
    
    //以下自定义方法
    
    /**
     * 查询所有白名单过滤信息
     * @return
     */
    List<Filtering> selectByAll();
    
    /**
     * 查询所有白名单信息总数
     * @param map
     * @param pageBounds
     * @return
     */
    Integer countFiltering(Map<String, Object> map);
    
    /***
     * 获取设备是否为白名单
     * @param sn
     * @param mac
     * @param model
     * @return
     */
    Integer getFiltering(String sn,String mac,String model);
    
    /**
     * 返回设备黑白名单状态，web端添加修改判断
     * @return
     */
    List<Filtering> getBWList(String model);
    
    /**
     * 返回设备黑白名单状态，终端接口使用拦截
     * @return
     */
    int getBWStatus(Filtering record);
  
}
