package com.appStore.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.appStore.entity.XmlFilter;
import com.appStore.service.XmlFilterService;

@Controller
public class XmlFilterHandler {
	@Autowired
	private XmlFilterService xmlFilterService;
	
	/**
	 * 查询所有静默安装信息
	 * @param request
	 * @return
	 */
	@RequestMapping("getXmlFilter")
	@ResponseBody
	public List<XmlFilter> getXmlFilter(HttpServletRequest request) {	
		List<XmlFilter> list = new ArrayList<XmlFilter>();
		try {
			//所有数据信息
			list = xmlFilterService.getByXmlFilterList();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 添加OTA升级Mac过滤拦截
	 * @param stat
	 * @return
	 */
	@RequestMapping(value = "add-XmlFilter")
	@ResponseBody
	public JSONObject addXmlFilter(@RequestBody XmlFilter filter) {
		JSONObject json = new JSONObject();
		System.out.println("添加OTA升级SN过滤拦截的状态码: " + filter.getStatus());
		try {
			List<XmlFilter> list = xmlFilterService.getByXmlFilterList();
			if(list.size() != 0) {
				json.put("code", 0);
				json.put("msg", "Exist SN Address Filtering!");
				json.put("data", null);
				return json;
			}
			int ret = xmlFilterService.insertSelective(filter);
			if(ret>0) {
				json.put("code", 1);
				json.put("msg", "Added successfully!");
				json.put("data", null);
			}else {
				json.put("code", 0);
				json.put("msg", "Add failed!");
				json.put("data", null);
			}
		} catch (Exception exception) {
			json.put("code", -1);
			json.put("msg", "System Exception!");
			json.put("data", null);
			exception.printStackTrace();
		}
		return json;
	}
	
	/**
	 * 删除OTA升级Mac过滤拦截
	 * @param stat
	 * @return
	 */
	@RequestMapping(value = "delete-XmlFilter")
	@ResponseBody
	public JSONObject deleteXmlFilter(@RequestBody XmlFilter filter) {
		JSONObject json = new JSONObject();
		try {
			int ret = xmlFilterService.deleteByPrimaryKey(filter.getId());
			if(ret>0) {
				json.put("code", 1);
				json.put("msg", "Delete Successfully!");
				json.put("data", null);
			}else {
				json.put("code", 0);
				json.put("msg", "Delete Failed!");
				json.put("data", null);
			}
		} catch (Exception exception) {
			json.put("code", -1);
			json.put("msg", "System Exception!");
			json.put("data", null);
			exception.printStackTrace();
		}
		return json;
	}
	
	/**
	 * 编辑静默安装信息
	 * @param stat
	 * @return
	 */
	@RequestMapping(value = "edit-XmlFilter")
	@ResponseBody
	public JSONObject editXmlFilter(@RequestBody XmlFilter filter) {
		JSONObject json = new JSONObject();
		System.out.println("编辑xml过滤SN的编号: " + filter.getId());
		try {
			int ret = xmlFilterService.updateByPrimaryKeySelective(filter);
			if(ret>0) {
				json.put("code", 1);
				json.put("msg", "Successfully modified!");
				json.put("data", null);
			}else {
				json.put("code", 0);
				json.put("msg", "fail to edit!");
				json.put("data", null);
			}
		} catch (Exception exception) {
			json.put("code", -1);
			json.put("msg", "System Exception!");
			json.put("data", null);
			exception.printStackTrace();
		}
		return json;
	}
}
