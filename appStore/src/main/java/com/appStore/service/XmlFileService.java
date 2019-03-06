package com.appStore.service;

import java.util.List;

import com.appStore.entity.XmlFile;

public interface XmlFileService {
    int deleteByPrimaryKey(Integer id);

    int insert(XmlFile record);

    int insertSelective(XmlFile record);

    XmlFile selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(XmlFile record);

    int updateByPrimaryKey(XmlFile record);
    
    /////以下自定义方法
    /**
     ** 查询OTA升级xml文件路径
     * @param record
     * @return
     */
    XmlFile selectByXmlFile(XmlFile record);
    /**
     ** 查询所有OTA升级查询xml信息 
     * @param record
     * @return
     */
    List<XmlFile> selectByXmlFilerList();
 
}
