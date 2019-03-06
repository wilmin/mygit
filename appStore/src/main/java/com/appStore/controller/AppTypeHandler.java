package com.appStore.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.appStore.entity.AppType;
import com.appStore.service.AppTypeService;

@Controller
public class AppTypeHandler {
	@Autowired
	private AppTypeService appTypeService;
	
	/**
	 * 查询所有apk类型信息
	 * @param request
	 * @return
	 */
	@RequestMapping("getAppTypeAll")
	@ResponseBody
	public List<AppType> getAppTypeAll(HttpServletRequest request) {
		List<AppType> list = null;
		try {
			//所有数据信息
			list = appTypeService.selectByApkTypeAll();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return list;
	}
}
