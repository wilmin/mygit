package com.appStore.service;

import java.util.List;

import com.appStore.entity.ModelVersion;

public interface ModelVersionService {
	int deleteByPrimaryKey(Integer id);

    int insert(ModelVersion record);

    int insertSelective(ModelVersion record);

    ModelVersion selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ModelVersion record);

    int updateByPrimaryKey(ModelVersion record);
    
    /***
     * *查询所有机型版本信息
     * @return
     */
    List<ModelVersion> selectByAllModelVersion();
    /**
     * <!-- 安机型版本客户查询 -->
     * @return
     */
    ModelVersion selectByModelVersionStatus(ModelVersion record);
    /**
     * <!-- 通过的机型版本关联编号删除自动配置信息 -->
     * @param id
     * @return
     */
    int deleteByAutoModelVersionId(Integer id);
    /**
     * <!-- 通过的机型版本关联编号删除静默安装信息 -->
     * @param id
     * @return
     */
    int deleteByStaticModelVersionId(Integer id);
    /**
     * <!-- 通过的机型版本关联编号删除商城APP信息 -->
     * @param id
     * @return
     */
    int deleteByStoreModelVersionId(Integer id);
}
