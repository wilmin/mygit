package com.appStore.service;

import java.util.List;

import com.appStore.entity.XmlFilter;

public interface XmlFilterService {
	int deleteByPrimaryKey(Integer id);

    int insert(XmlFilter record);

    int insertSelective(XmlFilter record);

    XmlFilter selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(XmlFilter record);

    int updateByPrimaryKey(XmlFilter record);
	/**
     ** web端查询所有OTA升级Mac地址过滤信息
     * @return
     */
    List<XmlFilter> getByXmlFilterList();
    
    /**
     ** 获取终端OTA升级Mac地址过滤状态码
     * @return
     */
    int getByXmlMacFilerStatus(String mac);
}
