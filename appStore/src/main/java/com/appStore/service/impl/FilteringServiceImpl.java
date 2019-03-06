package com.appStore.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appStore.dao.FilteringMapper;
import com.appStore.entity.Filtering;
import com.appStore.service.FilteringService;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

@Service
public class FilteringServiceImpl implements FilteringService {
	@Autowired
	private FilteringMapper filteringMapper;
	@Override
	public Integer deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return filteringMapper.deleteByPrimaryKey(id);
	}

	@Override
	public Integer insert(Filtering record) {
		// TODO Auto-generated method stub
		return filteringMapper.insert(record);
	}

	@Override
	public Integer insertSelective(Filtering record) {
		// TODO Auto-generated method stub
		return filteringMapper.insertSelective(record);
	}

	@Override
	public Filtering selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return filteringMapper.selectByPrimaryKey(id);
	}

	@Override
	public Integer updateByPrimaryKeySelective(Filtering record) {
		// TODO Auto-generated method stub
		return filteringMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public Integer updateByPrimaryKey(Filtering record) {
		// TODO Auto-generated method stub
		return filteringMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<Filtering> selectByAll() {
		// TODO Auto-generated method stub
		return filteringMapper.selectByAll();
	}

	@Override
	public Integer getFiltering(String sn, String mac, String model) {
		// TODO Auto-generated method stub
		return filteringMapper.getFiltering(sn, mac, model);
	}

	@Override
	public Integer countFiltering(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return filteringMapper.countFiltering(map);
	}

	/***
	 * 返回设备黑白名单状态，web端添加修改判断
	 */
	@Override
	public List<Filtering> getBWList(String model) {
		return filteringMapper.getBWList(model);
	}
	/***
	 * 返回设备黑白名单状态,终端接口使用拦截
	 */
	@Override
	public int getBWStatus(Filtering record) {
	    String model = record.getModel();
	    String sn = record.getSn();
	    String mac = record.getMac();
	    int filterState = 0;
		try {
			List<Filtering> list = filteringMapper.getBWList(model);
			//过滤表不存在机型，没有黑白名单拦截，返回1，可以继续访问操作
			if(list.size()==0) {
				return 1;
			}
			//过滤表存在机型，存在黑白名单，继续判断
			else {
				for (Filtering filtering : list) {
					//SN和Mac为空，通过机型过滤拦截设备黑白名单
					if(filtering.getSn() == null && filtering.getMac() == null || filtering.getSn().equals("") && filtering.getMac().equals("")) {
						return filtering.getFilterState();
					}
					//若sn和Mac不为空，说明过滤单个设备
					else {
						//sn和Mac相等,返回字段状态的值
						if(sn.equals(filtering.getSn()) && mac.equals(filtering.getMac())) {
							return filtering.getFilterState();
						}
						//不相等，返回字段状态的相反值
						else {
							filterState = -filtering.getFilterState();
						}
					}
				}
			}
		} catch (Exception e) {
			//异常状态值
			filterState = 0;
			e.printStackTrace();
		}
		
		return filterState;
	}
	
	
}
