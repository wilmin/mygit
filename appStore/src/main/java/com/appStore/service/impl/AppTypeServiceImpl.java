package com.appStore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appStore.dao.AppTypeMapper;
import com.appStore.entity.AppType;
import com.appStore.service.AppTypeService;

@Service
public class AppTypeServiceImpl implements AppTypeService {
	@Autowired
	private AppTypeMapper appTypeMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return appTypeMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(AppType record) {
		// TODO Auto-generated method stub
		return appTypeMapper.insert(record);
	}

	@Override
	public int insertSelective(AppType record) {
		// TODO Auto-generated method stub
		return appTypeMapper.insertSelective(record);
	}

	@Override
	public AppType selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return appTypeMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(AppType record) {
		// TODO Auto-generated method stub
		return appTypeMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(AppType record) {
		// TODO Auto-generated method stub
		return appTypeMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<AppType> selectByApkTypeAll() {
		// TODO Auto-generated method stub
		return appTypeMapper.selectByApkTypeAll();
	}

}
