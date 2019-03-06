package com.appStore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appStore.dao.XmlFileMapper;
import com.appStore.entity.XmlFile;
import com.appStore.service.XmlFileService;

@Service
public class XmlFileServiceImpl implements XmlFileService {
	@Autowired
	private XmlFileMapper xmlFileMapper;
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return xmlFileMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(XmlFile record) {
		// TODO Auto-generated method stub
		return xmlFileMapper.insert(record);
	}

	@Override
	public int insertSelective(XmlFile record) {
		// TODO Auto-generated method stub
		return xmlFileMapper.insertSelective(record);
	}

	@Override
	public XmlFile selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return xmlFileMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(XmlFile record) {
		// TODO Auto-generated method stub
		return xmlFileMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(XmlFile record) {
		// TODO Auto-generated method stub
		return xmlFileMapper.updateByPrimaryKey(record);
	}

	@Override
	public XmlFile selectByXmlFile(XmlFile record) {
		// TODO Auto-generated method stub
		return xmlFileMapper.selectByXmlFile(record);
	}

	@Override
	public List<XmlFile> selectByXmlFilerList() {
		// TODO Auto-generated method stub
		return xmlFileMapper.selectByXmlFilerList();
	}


}
