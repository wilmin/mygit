package com.appStore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appStore.dao.ApkAutomaticConfigurationMapper;
import com.appStore.entity.ApkAutomaticConfiguration;
import com.appStore.service.ApkAutoConfigurationService;

@Service
public class ApkAutoConfigurationServiceImpl implements ApkAutoConfigurationService {
	@Autowired
	private ApkAutomaticConfigurationMapper  apkAutomaticConfigurationMapper;
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return apkAutomaticConfigurationMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(ApkAutomaticConfiguration record) {
		// TODO Auto-generated method stub
		return apkAutomaticConfigurationMapper.insert(record);
	}

	@Override
	public int insertSelective(ApkAutomaticConfiguration record) {
		// TODO Auto-generated method stub
		return apkAutomaticConfigurationMapper.insertSelective(record);
	}

	@Override
	public ApkAutomaticConfiguration selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return apkAutomaticConfigurationMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(ApkAutomaticConfiguration record) {
		// TODO Auto-generated method stub
		return apkAutomaticConfigurationMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(ApkAutomaticConfiguration record) {
		// TODO Auto-generated method stub
		return apkAutomaticConfigurationMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<ApkAutomaticConfiguration> selectByApkAutoList() {
		// TODO Auto-generated method stub
		return apkAutomaticConfigurationMapper.selectByApkAutoList();
	}

	@Override
	public ApkAutomaticConfiguration selectByApkAutoStatus(Integer id) {
		// TODO Auto-generated method stub
		return apkAutomaticConfigurationMapper.selectByApkAutoStatus(id);
	}

}
