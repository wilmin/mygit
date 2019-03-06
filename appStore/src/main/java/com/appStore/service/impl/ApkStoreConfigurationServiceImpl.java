package com.appStore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appStore.dao.ApkStoreConfigurationMapper;
import com.appStore.entity.ApkStoreConfiguration;
import com.appStore.service.ApkStoreConfigurationService;

@Service
public class ApkStoreConfigurationServiceImpl implements ApkStoreConfigurationService {
	@Autowired
	private ApkStoreConfigurationMapper apkStoreConfigurationMapper;
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return apkStoreConfigurationMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(ApkStoreConfiguration record) {
		// TODO Auto-generated method stub
		return apkStoreConfigurationMapper.insert(record);
	}

	@Override
	public int insertSelective(ApkStoreConfiguration record) {
		// TODO Auto-generated method stub
		return apkStoreConfigurationMapper.insertSelective(record);
	}

	@Override
	public ApkStoreConfiguration selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return apkStoreConfigurationMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(ApkStoreConfiguration record) {
		// TODO Auto-generated method stub
		return apkStoreConfigurationMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(ApkStoreConfiguration record) {
		// TODO Auto-generated method stub
		return apkStoreConfigurationMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<ApkStoreConfiguration> selectByApkStoreList() {
		// TODO Auto-generated method stub
		return apkStoreConfigurationMapper.selectByApkStoreList();
	}

	@Override
	public ApkStoreConfiguration selectByStoreStatus(Integer id) {
		// TODO Auto-generated method stub
		return apkStoreConfigurationMapper.selectByStoreStatus(id);
	}

	

}
