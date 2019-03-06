package com.appStore.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.appStore.entity.ModelVersion;
import com.appStore.service.ModelVersionService;

@Controller
public class ModelVersionHandler {
	@Autowired
	private ModelVersionService modelVersionService;
	
	/**
	 * 查询所有机型版本信息
	 * @param request
	 * @return
	 */
	@RequestMapping("getModelVersion")
	@ResponseBody
	public List<ModelVersion> getModelVersion(HttpServletRequest request) {
		List<ModelVersion> list = null;
		try {
			//所有数据信息
			list = modelVersionService.selectByAllModelVersion();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 添加机型版本信息
	 * @param obj
	 * @return
	 */
	@RequestMapping(value = "add-ModelVersion")
	@ResponseBody
	public JSONObject addModelVersion(@RequestBody ModelVersion obj) {
		JSONObject json = new JSONObject();
		System.out.println("添加机型版本信息的型号: " + obj.getModel());
		try {
			int ret = modelVersionService.insertSelective(obj);
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
	 * 编辑机型版本信息
	 * @param obj
	 * @return
	 */
	@RequestMapping(value = "edit-ModelVersion")
	@ResponseBody
	public JSONObject editModelVersion(@RequestBody ModelVersion obj) {
		JSONObject json = new JSONObject();
		System.out.println("编辑机型版本信息的型号: " + obj.getModel());
		try {
			int ret = modelVersionService.updateByPrimaryKeySelective(obj);
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
	 * 删除机型版本信息
	 * @param obj
	 * @return
	 */
	@Transactional
	@RequestMapping(value = "delete-ModelVersion")
	@ResponseBody
	public JSONObject deleteModelVersion(@RequestBody ModelVersion obj) {
		JSONObject json = new JSONObject();
		try {
			int id = obj.getId();
			int ret = modelVersionService.deleteByPrimaryKey(id);
			if(ret>0) {
				modelVersionService.deleteByAutoModelVersionId(id);
				modelVersionService.deleteByStaticModelVersionId(id);
				modelVersionService.deleteByStoreModelVersionId(id);
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
}
