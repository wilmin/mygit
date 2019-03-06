package com.appStore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appStore.dao.XmlFilterMapper;
import com.appStore.entity.XmlFilter;
import com.appStore.service.XmlFilterService;

@Service
public class XmlFilterServiceImpl implements XmlFilterService {
	@Autowired
	private XmlFilterMapper xmlFilterMapper;
	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return xmlFilterMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(XmlFilter record) {
		// TODO Auto-generated method stub
		return xmlFilterMapper.insert(record);
	}

	@Override
	public int insertSelective(XmlFilter record) {
		// TODO Auto-generated method stub
		return xmlFilterMapper.insertSelective(record);
	}

	@Override
	public XmlFilter selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return xmlFilterMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(XmlFilter record) {
		// TODO Auto-generated method stub
		return xmlFilterMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(XmlFilter record) {
		// TODO Auto-generated method stub
		return xmlFilterMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<XmlFilter> getByXmlFilterList() {
		// TODO Auto-generated method stub
		return xmlFilterMapper.getByXmlFilterList();
	}

	/**
	 * 获取终端OTA升级Mac地址过滤状态码
	 */
	@Override
	public int getByXmlMacFilerStatus(String mac) {
		try {
			//若没有烧录sn序列号时，传入字符串“unknown”
			if(("unknown").equals(mac)) {
				return 0;
			}
			//Mac字符串转换long类型
			long macNum = Long.parseLong(mac, 16);
			System.out.println("sn转换long类型："+macNum);
			List<XmlFilter> list = xmlFilterMapper.getByXmlFilterList();
			if(list.size()==0) {
				return 1;
			}else {
				for (XmlFilter xmlFilter : list) {
					System.out.println(xmlFilter.getBeginmac() == null && xmlFilter.getEndmac() == null || xmlFilter.getBeginmac().equals("") && xmlFilter.getEndmac().equals(""));
					//开始和结束Mac为空,返回过滤状态码
					if(xmlFilter.getBeginmac() == null && xmlFilter.getEndmac() == null || xmlFilter.getBeginmac().equals("") && xmlFilter.getEndmac().equals("")) {
						return xmlFilter.getStatus();
					}
					//开始和结束Mac不为空，说明过滤Mac范围
					else {
						//比较Mac地址范围
						long beginMac = Long.parseLong(xmlFilter.getBeginmac(), 16);
						long endMac = Long.parseLong(xmlFilter.getEndmac(), 16);
						System.out.println(beginMac <= macNum && macNum <= endMac);
						if(beginMac <= macNum && macNum <= endMac) {
							return xmlFilter.getStatus();
						}
						//不相等，返回字段状态的相反值
						else {
							return -xmlFilter.getStatus();
						}
					}
				
				}
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

}
