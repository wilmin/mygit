package com.appStore.dao;

import java.util.List;

import com.appStore.entity.XmlFilter;

public interface XmlFilterMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(XmlFilter record);

    int insertSelective(XmlFilter record);

    XmlFilter selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(XmlFilter record);

    int updateByPrimaryKey(XmlFilter record);
    
    /**
     * 查询OTA升级过滤Mac地址的信息列表
     * @return
     */
    List<XmlFilter> getByXmlFilterList();
}