package com.appStore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appStore.dao.ApkStaticConfigurationMapper;
import com.appStore.entity.ApkStaticConfiguration;
import com.appStore.service.ApkStaticConfigurationService;

@Service
public class ApkStaticConfigurationServiceImpl implements ApkStaticConfigurationService {
	@Autowired
	private ApkStaticConfigurationMapper apkStaticConfigurationMapper;
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return apkStaticConfigurationMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(ApkStaticConfiguration record) {
		// TODO Auto-generated method stub
		return apkStaticConfigurationMapper.insert(record);
	}

	@Override
	public int insertSelective(ApkStaticConfiguration record) {
		// TODO Auto-generated method stub
		return apkStaticConfigurationMapper.insertSelective(record);
	}

	@Override
	public ApkStaticConfiguration selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return apkStaticConfigurationMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(ApkStaticConfiguration record) {
		// TODO Auto-generated method stub
		return apkStaticConfigurationMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(ApkStaticConfiguration record) {
		// TODO Auto-generated method stub
		return apkStaticConfigurationMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<ApkStaticConfiguration> selectByApkStaticList() {
		// TODO Auto-generated method stub
		return apkStaticConfigurationMapper.selectByApkStaticList();
	}

	@Override
	public ApkStaticConfiguration selectByApkStaticStatus(Integer id) {
		// TODO Auto-generated method stub
		return apkStaticConfigurationMapper.selectByApkStaticStatus(id);
	}

}
