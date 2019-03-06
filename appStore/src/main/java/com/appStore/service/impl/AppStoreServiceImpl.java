package com.appStore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appStore.dao.AppstoreMapper;
import com.appStore.entity.Appstore;
import com.appStore.service.AppStoreService;

@Service
public class AppStoreServiceImpl implements AppStoreService {
	@Autowired
	private AppstoreMapper appstoreMapper;
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return appstoreMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(Appstore record) {
		// TODO Auto-generated method stub
		return appstoreMapper.insert(record);
	}

	@Override
	public int insertSelective(Appstore record) {
		// TODO Auto-generated method stub
		return appstoreMapper.insertSelective(record);
	}

	@Override
	public Appstore selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return appstoreMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(Appstore record) {
		// TODO Auto-generated method stub
		return appstoreMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKeyWithBLOBs(Appstore record) {
		// TODO Auto-generated method stub
		return appstoreMapper.updateByPrimaryKeyWithBLOBs(record);
	}

	@Override
	public int updateByPrimaryKey(Appstore record) {
		// TODO Auto-generated method stub
		return appstoreMapper.updateByPrimaryKey(record);
	}

	@Override
	public Integer selectlastid() {
		// TODO Auto-generated method stub
		return appstoreMapper.selectlastid();
	}

	@Override
	public List<Appstore> getByAppstoreAll() {
		// TODO Auto-generated method stub
		return appstoreMapper.getByAppstoreAll();
	}

	@Override
	public Integer updateForDownloadTotal(Integer id) {
		// TODO Auto-generated method stub
		return appstoreMapper.updateForDownloadTotal(id);
	}

	@Override
	public List<Appstore> selectAllAppstoreInfoWithFilter() {
		// TODO Auto-generated method stub
		return appstoreMapper.selectAllAppstoreInfoWithFilter();
	}

	@Override
	public Appstore selectByApkInfo(Appstore record) {
		// TODO Auto-generated method stub
		return appstoreMapper.selectByApkInfo(record);
	}

}
