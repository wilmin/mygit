package com.appStore.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.appStore.entity.Filtering;
import com.appStore.service.FilteringService;
import com.appStore.tools.OTAToolKit;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

@Controller
public class FilteringHandler {
	@Autowired
	HttpSession session;
	@Autowired
	private FilteringService filteringService;
	
	/**
	 * 查询所有白名单信息
	 * @param request
	 * @return
	 */
	@RequestMapping("getFiltering")
	@ResponseBody
	public List<Filtering> getFiltering() {
		List<Filtering> filterList = new ArrayList<Filtering>();
		try {
			//所有数据信息
			filterList = filteringService.selectByAll();
			
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return filterList;
	}

	/**
	 * 添加白名单信息信息
	 * @param auto
	 * @return
	 */
	@RequestMapping(value = "add-WhiteList")
	@ResponseBody
	public JSONObject addWhiteList(@RequestBody Filtering filter) {
		JSONObject json = new JSONObject();
		try {
			int ret = filteringService.insertSelective(filter);
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
	 * 编辑白名单信息信息
	 * @param auto
	 * @return
	 */
	@RequestMapping(value = "update-WhiteList")
	@ResponseBody
	public JSONObject updateWhiteList(@RequestBody Filtering filter) {
		JSONObject json = new JSONObject();
		try {
			int ret = filteringService.updateByPrimaryKeySelective(filter);
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
	/**
	 * 删除白名单信息信息
	 * @param filter
	 * @return
	 */
	@RequestMapping(value = "delete-WhiteList")
	@ResponseBody
	public JSONObject deleteWhiteList(@RequestBody Filtering filter) {
		JSONObject json = new JSONObject();
		try {
			int ret = filteringService.deleteByPrimaryKey(filter.getId());
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
	 * 检测黑白名单是否重复存在
	 * @param filter
	 * @return
	 */
	@RequestMapping(value = "check-WhiteList")
	@ResponseBody
	public JSONObject checkWhiteList(@RequestBody Filtering filter) {
		JSONObject json = new JSONObject();
		String model = filter.getModel();
	    String sn = filter.getSn();
	    String mac = filter.getMac();
	    int status = filter.getFilterState();
	    int filterStatus = 0;
		try {
			List<Filtering> list = filteringService.getBWList(model);
			//过滤表不存在机型，没有黑白名单拦截，返回0，此机型可以随便添加黑白名单
			if(list.size()==0) {
				json.put("code", 0);
				json.put("msg","");
				json.put("data", null);
			}
			//过滤表存在机型，存在黑白名单，继续判断
			else {
				for (Filtering filtering : list) {
					filterStatus = filtering.getFilterState();
					//黑白名单判断
					if(status != filterStatus) {
						if(filterStatus==1) {
							json.put("msg", "Whitelist exists for this model.");
						}else {
							json.put("msg", "Blacklist exists for this model.");
						}
						json.put("code",1);
						json.put("data", "sn-mac-isnone");
						return json;
					}
					System.out.println(filtering.getSn() == null && filtering.getMac() == null || filtering.getSn().equals("") && filtering.getMac().equals(""));
					//SN和Mac为空，通过机型过滤拦截设备黑白名单
					if(filtering.getSn() == null && filtering.getMac() == null || filtering.getSn().equals("") && filtering.getMac().equals("")) {
						if(filterStatus==1) {
							json.put("msg", "The model has been whitelisted.");
						}else {
							json.put("msg", "The model has been blacklisted.");
						}
						json.put("code",1);
						json.put("data", "sn-mac-isnone");
						return json;
					}
					//若sn和Mac不为空，说明过滤单个设备
					else {
						//sn和Mac相等,存在设备信息
						if((sn.equals(filtering.getSn()) && mac.equals(filtering.getMac())) || (sn.equals("") && mac.equals(""))) {
							if(filterStatus==1) {
								json.put("msg", "Device has been whitelisted.");
							}else {
								json.put("msg", "Device has been blacklisted.");
							}
							json.put("code",1);
							json.put("data", "sn-mac");
							return json;
						}
						//不相等，不存设备信息
						else {
							json.put("code", 0);
							json.put("msg", "");
							json.put("data", null);
						}
					}
					
				}
			}
		} catch (Exception e) {
			//异常状态值
			json.put("code", -1);
			json.put("msg", "System Exception!");
			json.put("data", null);
			e.printStackTrace();
		}
		return json;
	}
	
}
