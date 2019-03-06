package com.appStore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appStore.dao.ModelVersionMapper;
import com.appStore.entity.ModelVersion;
import com.appStore.service.ModelVersionService;

@Service
public class ModelVersionServiceImpl implements ModelVersionService {
	@Autowired
	private ModelVersionMapper modelVersionMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return modelVersionMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(ModelVersion record) {
		// TODO Auto-generated method stub
		return modelVersionMapper.insert(record);
	}

	@Override
	public int insertSelective(ModelVersion record) {
		// TODO Auto-generated method stub
		return modelVersionMapper.insertSelective(record);
	}

	@Override
	public ModelVersion selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return modelVersionMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(ModelVersion record) {
		// TODO Auto-generated method stub
		return modelVersionMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(ModelVersion record) {
		// TODO Auto-generated method stub
		return modelVersionMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<ModelVersion> selectByAllModelVersion() {
		// TODO Auto-generated method stub
		return modelVersionMapper.selectByAllModelVersion();
	}

	@Override
	public ModelVersion selectByModelVersionStatus(ModelVersion record) {
		// TODO Auto-generated method stub
		return modelVersionMapper.selectByModelVersionStatus(record);
	}

	@Override
	public int deleteByAutoModelVersionId(Integer id) {
		// TODO Auto-generated method stub
		return modelVersionMapper.deleteByAutoModelVersionId(id);
	}

	@Override
	public int deleteByStaticModelVersionId(Integer id) {
		// TODO Auto-generated method stub
		return modelVersionMapper.deleteByStaticModelVersionId(id);
	}

	@Override
	public int deleteByStoreModelVersionId(Integer id) {
		// TODO Auto-generated method stub
		return modelVersionMapper.deleteByStoreModelVersionId(id);
	}

}
