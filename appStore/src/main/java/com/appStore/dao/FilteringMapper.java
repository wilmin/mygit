package com.appStore.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.appStore.entity.Filtering;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

public interface FilteringMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Filtering record);

    int insertSelective(Filtering record);

    Filtering selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Filtering record);

    int updateByPrimaryKey(Filtering record);
    
    //以下自定义方法
    
    /**
     * 查询所有白名单过滤信息
     * @return
     */
    List<Filtering> selectByAll();
    /**
     * 查询所有白名单总数
     * @param map
     * @param pageBounds
     * @return
     */
    Integer countFiltering(Map<String, Object> map);
    /**
     * 获取设备是否为白名单
     * @param sn
     * @param mac
     * @param model
     * @return
     */
    Integer getFiltering(@Param("sn")String sn,@Param("mac")String mac,@Param("model")String model);
    
    /**
     * 终端机型过滤拦截查询黑白名单
     * @return
     */
    List<Filtering> getBWList(@Param("model")String model);
}