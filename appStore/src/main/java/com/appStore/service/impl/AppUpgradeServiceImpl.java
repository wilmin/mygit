package com.appStore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appStore.dao.AppUpgradeMapper;
import com.appStore.entity.AppUpgrade;
import com.appStore.service.AppUpgradeService;

@Service
public class AppUpgradeServiceImpl implements AppUpgradeService {
	@Autowired
	private AppUpgradeMapper appUpgradeMapper;
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return appUpgradeMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(AppUpgrade record) {
		// TODO Auto-generated method stub
		return appUpgradeMapper.insert(record);
	}

	@Override
	public int insertSelective(AppUpgrade record) {
		// TODO Auto-generated method stub
		return appUpgradeMapper.insertSelective(record);
	}

	@Override
	public AppUpgrade selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return appUpgradeMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(AppUpgrade record) {
		// TODO Auto-generated method stub
		return appUpgradeMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(AppUpgrade record) {
		// TODO Auto-generated method stub
		return appUpgradeMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<AppUpgrade> getAppUpgradeAll() {
		// TODO Auto-generated method stub
		return appUpgradeMapper.getAppUpgradeAll();
	}

	@Override
	public AppUpgrade selectByStoreApkIdList(Integer id) {
		// TODO Auto-generated method stub
		return appUpgradeMapper.selectByStoreApkIdList(id);
	}

	@Override
	public List<AppUpgrade> selectByDefaultStoreApkList(String android) {
		// TODO Auto-generated method stub
		return appUpgradeMapper.selectByDefaultStoreApkList(android);
	}

	@Override
	public List<AppUpgrade> selectByStoreApkList() {
		// TODO Auto-generated method stub
		return appUpgradeMapper.selectByStoreApkList();
	}

	@Override
	public List<AppUpgrade> selectByAutoStaticApkList() {
		// TODO Auto-generated method stub
		return appUpgradeMapper.selectByAutoStaticApkList();
	}

	@Override
	public AppUpgrade selectByNameApkList(AppUpgrade record) {
		// TODO Auto-generated method stub
		return appUpgradeMapper.selectByNameApkList(record);
	}


}
