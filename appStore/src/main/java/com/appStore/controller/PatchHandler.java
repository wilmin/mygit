package com.appStore.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.appStore.entity.XmlFile;
import com.appStore.service.XmlFileService;
import com.appStore.tools.AmazonS3Utils;
import com.appStore.tools.FileUtil;
import com.appStore.tools.OTAToolKit;

@Controller
public class PatchHandler {
	@Autowired
	private XmlFileService xmlFileService;
	@Autowired
	private AmazonS3Utils S3Util;
	// 获取上传s3的xml文件的key
	private String s3XmlKey = "";
	// 上传的xml文件的下载URL
	private String downloadUrl;

	/**
	 * *查询所有OTA升级的xml文件信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("patch-info")
	@ResponseBody
	public List<XmlFile> pathcInfo(HttpServletRequest request) {
		List<XmlFile> list = null;
		try {
			list = xmlFileService.selectByXmlFilerList();
			System.out.println(list.size());
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return list;
	}

	/***
	 * *删除xml文件信息
	 * 
	 * @param req
	 * @param id
	 * @return
	 */
	@Transactional
	@RequestMapping("deleteXmlFile")
	@ResponseBody
	public JSONObject deleteXmlFile(HttpServletRequest req, @RequestBody JSONObject id) {
		JSONObject json = new JSONObject();
		System.out.println("id:" + id.getString("id"));
		int deleteid = Integer.parseInt(id.getString("id"));
		String path = req.getSession().getServletContext().getRealPath("/");
		XmlFile fileInfo = xmlFileService.selectByPrimaryKey(deleteid);
		if (fileInfo != null) {
			String filePath = path + "/" + fileInfo.getXmlurl();
			try {
				System.out.println("=========打印删除文件路径=========" + filePath);
				// 调用工具类，删除上传文件
				FileUtil.deleteFile(filePath);
				// 通过key删除S3上的文件
				S3Util.amazonS3DeleteObject(fileInfo.getS3xmlkey());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			int ret = xmlFileService.deleteByPrimaryKey(deleteid);
			if (ret > 0)
				json.put("msg", "Successfully deleted xml file information.");
			else
				json.put("msg", "Failed to delete xml file information.");
			json.put("code", 1);
			json.put("data", null);
		} catch (Exception e) {
			json.put("code", -1);
			json.put("msg", "failed");
			json.put("data", null);
			e.printStackTrace();
		}

		return json;
	}

	/**
	 * *点击提交按钮，上传xml文件和对应信息
	 * 
	 * @param updateFileInfo
	 * @return
	 */
	@Transactional
	@RequestMapping("addUpdateXMLFile")
	@ResponseBody
	public JSONObject addUpdateXMLFile(@RequestBody XmlFile fileInfo) {
		JSONObject json = new JSONObject();
		try {
			fileInfo.setXmlurl(downloadUrl);
			fileInfo.setS3xmlkey(s3XmlKey);
			fileInfo.setUploadtime(new Date());
			int ret = xmlFileService.insertSelective(fileInfo);
			if (ret > 0)
				json.put("msg", "Submit file information successfully.");
			else
				json.put("msg", "Submit file information failed.");
			json.put("code", 1);
			json.put("data", null);
		} catch (Exception e) {
			json.put("code", -1);
			json.put("msg", "System exception.");
			json.put("data", null);
			e.printStackTrace();
		}

		return json;
	}

	/**
	 * *上传xml文件
	 * 
	 * @param request
	 * @param file
	 * @return
	 */
	@Transactional
	@RequestMapping(value = "/uploadXMLFilter", method = RequestMethod.POST)
	public @ResponseBody JSONObject uploadImg(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		JSONObject json = new JSONObject();
		// 上传文件每日单独保存,获取服务器下的upload的路径
//		String path = request.getSession().getServletContext().getRealPath("upload");
		// 获取当日时间
		String systemdate = OTAToolKit.getSystemDate();
		// 上传的s3路径
		String s3Path = "otaUpdate";
		s3Path = OTAToolKit.getOSUrl(s3Path, systemdate);
		// 重新组建路径
//		path = path + "/" + systemdate;
		// 获取文件名，通过时间戳重新命名文件名称
		String fileName = OTAToolKit.addTimeStamp(file.getOriginalFilename());
		System.out.println("s3Path:" + s3Path);
		System.out.println("fileName:" + fileName);
		s3XmlKey = s3Path + "/" + fileName;
		try {
			// 调用S3上传工具上传xml文件,返回公开的URL
			downloadUrl = S3Util.amazonS3Upload(s3XmlKey, file.getBytes());
			// 上传文件，调用工具类
			/*
			 * FileUtil.uploadFile(file.getBytes(), path+"/", fileName); downloadUrl =
			 * "upload/" + systemdate + "/" + fileName;
			 */
			System.out.println(downloadUrl);
			json.put("code", 1);
			json.put("msg", "Successfully uploading files.");
			json.put("data", null);
		} catch (Exception e) {
			json.put("code", -1);
			json.put("msg", "System exception.");
			json.put("data", null);
			e.printStackTrace();
		}
		return json;
	}

}
