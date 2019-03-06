package com.appStore.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import com.appStore.entity.Box;
import com.appStore.service.BoxService;



@Controller
public class DeviceHandlers {
	@Autowired
	BoxService boxService;
	
	/**
	 * 查询所有访问服务器的盒子信息
	 * @param request
	 * @return
	 */
	@RequestMapping("deviceinfo")
	@ResponseBody
	public List<Box> deviceInfo(HttpServletRequest request) {
		List<Box> boxlist = null;
		try {
			boxlist = boxService.selectBoxWithFilter();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return boxlist;

	}
	
}


