package com.appStore.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import com.appStore.entity.ApkInfo;
import com.appStore.entity.AppType;
import com.appStore.entity.AppUpgrade;
import com.appStore.entity.Appstore;
import com.appStore.entity.AppstoreWithAllInfo;
import com.appStore.service.AppStoreService;
import com.appStore.service.AppTypeService;
import com.appStore.service.AppUpgradeService;
import com.appStore.tools.AmazonS3Utils;
import com.appStore.tools.ApkIconUtil;
import com.appStore.tools.ApkUtil;
import com.appStore.tools.FileUtil;
import com.appStore.tools.OTAToolKit;

@Controller
public class AppStoreHandler {
	@Autowired
	private AppStoreService appStoreService;
	@Autowired
	private AppTypeService appTypeService;
	@Autowired
	private AppUpgradeService appUpgradeService;

	@Autowired
	private AmazonS3Utils S3Util;
	//apk下载路径
	private String appstoredownloadUrl = "";
	//图标下载路径
	private String appstoreIcondownloadUrl = "";
	// 截屏图片路径
	private String appScreenshotUrl = "";
	
	//获取上传s3的apk的key
	private String s3AppKey = "";
	//获取上传s3的图标的key
	private String s3IconKey = "";
	//获取上传s3的截图的key
	private String s3ScreenshotKey = "";
	
	private Map<String, String> mapUrl = new HashMap<>();

	private String fileMD5;

	private long size;

	private long iconSize;

	/***
	 * *查看apk信息列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("store-info")
	@ResponseBody
	public List<AppstoreWithAllInfo> storeInfo(HttpServletRequest request) {
		List<AppstoreWithAllInfo> appstoresinfo = new ArrayList<AppstoreWithAllInfo>();
		try {
			List<Appstore> listApp = appStoreService.selectAllAppstoreInfoWithFilter();
			if (listApp.size() != 0) {
				for (Appstore appstore : listApp) {
					// 查询apk类型表
					AppType type = appTypeService.selectByPrimaryKey(appstore.getApktype());
					AppstoreWithAllInfo app = new AppstoreWithAllInfo();
					// apk类型编号
					app.setTypeId(type.getId());
					// apk类型名称
					app.setTypeName(type.getName());
					// apk编号
					app.setId(appstore.getId());
					// apk包名
					app.setApkpackagename(appstore.getApkpackagename());
					// apk版本号
					app.setApkversion(appstore.getApkversion());
					// apk版本名称
					app.setApkversionname(appstore.getApkversionname());
					// apk上传时间
					app.setUpdateTime(appstore.getUpdateTime());
					// apk大小
					app.setApksize(appstore.getApksize());
					// apk下载URL
					app.setApkdownload(appstore.getApkdownload());
					// apk下载次数
					app.setDownloadtotal(appstore.getDownloadtotal());
					// apk名称
					app.setApkname(appstore.getApkname());
					// apk图标url
					app.setApkicon(appstore.getApkicon());
					// apk简短说明
					app.setApkprofile(appstore.getApkprofile());
					// apk截图url
					app.setApkscreenshot(appstore.getApkscreenshot());
					// apk完整描述
					app.setApkinfo(appstore.getApkinfo());
					// apk的MD5校验
					app.setMd5(appstore.getMd5());
					appstoresinfo.add(app);

				}
			}

		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return appstoresinfo;

	}

	/**
	 * 删除apk信息列表
	 * 
	 * @param id
	 * @return
	 */
	@Transactional
	@RequestMapping("deleteAppFile")
	@ResponseBody
	public JSONObject deleteAppFile(@RequestBody JSONObject id, HttpServletRequest request) {
		JSONObject json = new JSONObject();
		Integer deleteid = Integer.parseInt(id.getString("id"));

		try {
			Appstore appstore = appStoreService.selectByPrimaryKey(deleteid);
			int ret = appStoreService.deleteByPrimaryKey(deleteid);
			// apk删除成功后，删除关联表上的信息
			if (ret > 0) {
				// 删除上传的对应文件
//				deleteXmlFile(appstore, request);
				//通过key删除s3上的文件
				deleteXmlFile(appstore.getS3appkey());
				deleteXmlFile(appstore.getS3iconkey());
				deleteXmlFile(appstore.getS3screenshotkey());
				
				List<AppUpgrade> list = appUpgradeService.getAppUpgradeAll();
				for (AppUpgrade app : list) {
					// apkid集合字段
					String apkIdList = app.getApkidlist();
					if (app.getApkidlist() == null || ("").equals(app.getApkidlist()))
						continue;
					// 重新整理apkid集合字段
					String newApkIdList = "";

					// 拆开APP升级列表对应的apkid字段
					String[] arr = apkIdList.split(",");
					for (String str : arr) {
						if (str.equals(String.valueOf(deleteid))) {
							System.out.println("删除apk时编号对比："+str.equals(String.valueOf(deleteid)));
							continue;
						}
						newApkIdList += str + ",";
					}
					// apklistid列重新修改赋值对象
					app.setApkidlist(newApkIdList);
					// 修改apklistid列
					appUpgradeService.updateByPrimaryKeySelective(app);

				}
				json.put("id", deleteid);
				json.put("code", 1);
				json.put("msg", "success");
			}
		} catch (Exception e) {
			json.put("code", -1);
			json.put("msg", "failed");
			e.printStackTrace();
		}

		return json;
	}

	/**
	 * 通过key删除S3上的文件
	 * @param keyList
	 */
	public void deleteXmlFile(String keyList) {
		String[] arr = keyList.split(",");
		for (String key : arr) {
			System.out.println("删除S3的文件key：" + key);
			S3Util.amazonS3DeleteObject(key);
		}
	}
	
	/**
	 * 删除上传的对应文件
	 * 
	 * @param appstore
	 */
	public void deleteXmlFile(Appstore appstore, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");

		if (appstore != null) {
			// apk的安装包url
			String apkPath = path + "/" + appstore.getApkdownload();
			// apk的图标url
			String iconPath = path + "/" + appstore.getApkicon();
			// apk的截图url
			String screenshotPath = path + "/" + appstore.getApkscreenshot();
			try {
				System.out.println("=========打印删除文件路径=========" + apkPath);
				// 调用工具类，删除上传文件
				FileUtil.deleteFile(apkPath);
				// 删除图标文件
				FileUtil.deleteFile(iconPath);
				// 删除截图文件
				if (screenshotPath != null) {
					String[] arr = screenshotPath.split(",");
					for (String str : arr) {
						FileUtil.deleteFile(str);
					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 提交apk文件信息上传
	 */
	@Transactional
	@RequestMapping("addapkupdateFile")
	@ResponseBody
	public JSONObject addapkupdateFile(@RequestBody Appstore appstore) {
		JSONObject json = new JSONObject();
		try {
			// 获取map里面的截图路径
			Iterator<String> iter = mapUrl.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				String value = mapUrl.get(key);
				//s3的key
				s3ScreenshotKey = s3ScreenshotKey + key + ",";
				//url下载路径
				appScreenshotUrl = appScreenshotUrl + value + ",";
			}
			/*
			 * System.out.println("======打印截图路径=======" + appScreenshotUrl); String[] arr =
			 * appScreenshotUrl.split(","); for (String string : arr) {
			 * System.out.println("拆开查看：" + string); }
			 */
			//初始化s3的key
			appstore.setS3screenshotkey(s3ScreenshotKey);
			// 初始化截图路径
			appstore.setApkscreenshot(appScreenshotUrl);
			//清除s3的key
			s3ScreenshotKey = "";
			// 清掉截图路径，方便下一次再次上传apk信息
			appScreenshotUrl = "";
			// 清空map
			mapUrl.clear();
			// 如果URL传递的是空值，连接为上传的apk路径
			if (appstore.getApkdownload() == null) {
				appstore.setApkdownload(appstoredownloadUrl);
			}
			// 图标url
			appstore.setApkicon(appstoreIcondownloadUrl);
			appstore.setS3appkey(s3AppKey);
			appstore.setS3iconkey(s3IconKey);
			appstore.setUpdateTime(new Date());
			appstore.setApksize(size);
			appstore.setDownloadtotal(0);
			appstore.setApkiconsize(iconSize);
			appstore.setMd5(fileMD5);
			int autoid = appStoreService.insertSelective(appstore);
			int id = appStoreService.selectlastid();

			json.put("code", 1);
			json.put("msg", "success");
			json.put("id", id);
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", -1);
		}
		return json;
	}

	/**
	 * 上传apk安装包
	 * 
	 * @param request
	 * @param file
	 * @return
	 * @throws IOException
	 */
	@Transactional
	@RequestMapping(value = "store_uploadfile", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject uploadFileHandler(HttpServletRequest request, @RequestParam("file") MultipartFile file)
			throws IOException {
		JSONObject json = new JSONObject();
		System.out.println("开始上传apk安装包");
		String path = request.getSession().getServletContext().getRealPath("upload");
		String sysdate = OTAToolKit.getSystemDate();
		// 重新组建路径
//		path = OTAToolKit.getOSUrl(path, sysdate);
		//上传的s3路径
		String s3Path = "appStore";
		s3Path = OTAToolKit.getOSUrl(s3Path, sysdate);
		// 获取文件名，通过时间戳重新命名文件名称
		String fileName = OTAToolKit.addTimeStamp(file.getOriginalFilename());
//		String fileName = file.getOriginalFilename();
		System.out.println("path:" + path);
		System.out.println("fileName:" + fileName);
		
		//获取上传的s3路径URL
//		String amazonS3Url = S3Util.getAmazonS3Url(s3Path+"/"+fileName);
		//获取上传s3的apk的key
		s3AppKey = s3Path+"/"+fileName;
		
		File targetFile = new File(path + "/", fileName);
		System.out.println(targetFile.exists());
		// 文件存在是否存在
		if (targetFile.exists()) {
			json.put("msgcode", 0);
			return json;
		}
		//获取apk上传服务器的路径
		String apkPath = "";
		//创建apk安装包的对象
		ApkInfo apkInfo = new ApkInfo();
		try {
			// 上传文件，调用工具类
			FileUtil.uploadFile(file.getBytes(), path + "/", fileName);
			//获取MD5
			fileMD5 = OTAToolKit.getMd5ByFile(targetFile);
			//大小
			size = targetFile.length();
			// 获取apk上传路径文件
			apkPath = path + "/" + fileName;
			// 读取apk安装包的信息
			apkInfo = new ApkUtil().getApkInfo(apkPath);
			System.out.println("打印出上传pak信息：" + apkInfo + "\t大小：" + size);
			//初始化数据
			Appstore recordApk = new Appstore();
			recordApk.setApkpackagename(apkInfo.getPackageName());
			recordApk.setApkversion(String.valueOf(apkInfo.getVersionCode()));
			recordApk.setApksize(size);
			recordApk.setMd5(fileMD5);
			//检测是否存在相同apk结果
			Appstore retApk = appStoreService.selectByApkInfo(recordApk);
			//存在相同apk
			if(retApk != null) {
				// 调用工具类，删除上传服务的文件
				try {
					FileUtil.deleteFile(apkPath);
					json.put("msgcode", 0);
					return json;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//json返回apk基本信息，页面显示
			json.put("ApkInfo", apkInfo);
			// 从APK里解压出icon图片并存放到磁盘上工具类
			ApkIconUtil iconUtil = new ApkIconUtil();
			long now = System.currentTimeMillis();
			 
			// S3图标的下载路径key
			s3IconKey = s3Path +"/" + now + ".png";
			// 从指定的apk文件里解压出icon图片并存放到指定的磁盘上 "D:\\image\\apkIcon" + now + ".png"
//			iconUtil.extractFileFromApk(apkPath, apkInfo.getApplicationIcon(), path + "/" + now + ".png");
			
			try {
				//获取图标，转换成byte类型
				byte[] data = iconUtil.InputStream2ByteArray(apkPath,apkInfo.getApplicationIcon());
				
				//调用上传s3服务工具,获取公有的URL下载路径[图标]
				appstoreIcondownloadUrl = S3Util.amazonS3Upload(s3IconKey,data);
				//调用上传s3服务工具,获取公有的URL下载路径[apk]
				appstoredownloadUrl = S3Util.amazonS3Upload(s3AppKey,file.getBytes());
				//关闭date流
				if(data != null) {
					data.clone();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(appstoredownloadUrl);
			json.put("apkdownload", appstoredownloadUrl);
			json.put("msgcode", 1);
		} catch (Exception e) {
			e.printStackTrace();
			json.put("msgcode", -1);
		}finally {
			// 调用工具类，删除上传服务的文件
			try {
				FileUtil.deleteFile(apkPath);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("json的值：" + json.getString("ApkInfo"));
		return json;
	}

	/**
	 * 
	 * 上传apk屏幕截图
	 * 
	 * @param request
	 * @param file
	 * @return
	 * @throws IOException
	 */
	@Transactional
	@RequestMapping(value = "store_uploadScreenshot", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject uploadScreenshotHandler(HttpServletRequest request, @RequestParam("file") MultipartFile file)
			throws IOException {
		JSONObject json = new JSONObject();
		System.out.println("开始上传截图");
		/*
		 * // 上传文件每日单独保存,获取服务器下的upload的路径
		 * String path = request.getSession().getServletContext().getRealPath("upload"); 
		 * // 重新组建路径
		 * path = path + "/" + systemdate;
		 */
		// 获取当日时间
		String systemdate = OTAToolKit.getSystemDate();
		//上传的s3路径
		String s3Path = "appStore";
		s3Path = OTAToolKit.getOSUrl(s3Path, systemdate);
		
		// 获取文件名，通过时间戳重新命名文件名称
		String fileName = OTAToolKit.addTimeStamp(file.getOriginalFilename());
		System.out.println("s3Path:" + s3Path);
		System.out.println("fileName:" + fileName);
		//获取上传s3的截图的key
		String s3Key = s3Path+"/"+fileName;
		try {
			//调用S3上传工具上传xml文件
			String url = S3Util.amazonS3Upload(s3Key,file.getBytes());
			// 上传文件，调用工具类
			/*
			 * FileUtil.uploadFile(file.getBytes(), path + "/", fileName); 
			 * String url = "upload/" + systemdate + "/" + fileName;
			 */
			System.out.println(url);
			// 路径url存入Map中
			mapUrl.put(s3Key, url);

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
