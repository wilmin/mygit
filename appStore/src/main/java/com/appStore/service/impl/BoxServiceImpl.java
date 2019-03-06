package com.appStore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.appStore.dao.BoxMapper;
import com.appStore.entity.Box;
import com.appStore.service.BoxService;

@Component		//定时任务注解
@Service
public class BoxServiceImpl implements BoxService {
	@Autowired
	private BoxMapper boxMapper;
	
	@Override
	public int deleteByPrimaryKey(String mac) {
		// TODO Auto-generated method stub
		return boxMapper.deleteByPrimaryKey(mac);
	}

	@Override
	public int insert(Box record) {
		// TODO Auto-generated method stub
		return boxMapper.insert(record);
	}

	@Override
	public int insertSelective(Box record) {
		// TODO Auto-generated method stub
		return boxMapper.insertSelective(record);
	}

	@Override
	public Box selectByPrimaryKey(String mac) {
		// TODO Auto-generated method stub
		return boxMapper.selectByPrimaryKey(mac);
	}

	@Override
	public int updateByPrimaryKeySelective(Box record) {
		// TODO Auto-generated method stub
		return boxMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(Box record) {
		// TODO Auto-generated method stub
		return boxMapper.updateByPrimaryKey(record);
	}

	@Override
	public Box selectMacSn(String mac, String sn) {
		// TODO Auto-generated method stub
		return boxMapper.selectMacSn(mac, sn);
	}


	@Override
	public List<Box> selectBoxWithFilter() {
		// TODO Auto-generated method stub
		return boxMapper.selectBoxWithFilter();
	}

	@Override
	public int updateByOTAPrimaryKeySelective(Box record) {
		// TODO Auto-generated method stub
		return boxMapper.updateByOTAPrimaryKeySelective(record);
	}

	@Override
	@Scheduled(cron = "0 0 0 * * ?")	//每天0点执行
	public int deleteByOneMonth() {
		// TODO Auto-generated method stub
		//定时删除一个月前的所有数据
		System.out.println("=============定时任务执行删除数据============");
		return boxMapper.deleteByOneMonth();
	}


}
