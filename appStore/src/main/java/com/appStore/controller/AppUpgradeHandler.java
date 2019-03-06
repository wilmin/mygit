package com.appStore.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.appStore.entity.ApkInfo;
import com.appStore.entity.AppStoreUpgrade;
import com.appStore.entity.AppType;
import com.appStore.entity.AppUpgrade;
import com.appStore.entity.Appstore;
import com.appStore.service.AppStoreService;
import com.appStore.service.AppUpgradeService;
import com.appStore.tools.IPAddressUtils;
import com.appStore.tools.Util;
import com.google.gson.Gson;

@Controller
public class AppUpgradeHandler {
	@Autowired
	private AppUpgradeService appUpgradeService;
	@Autowired
	private AppStoreService appStoreService;

	/**
	 * 查询所有APP升级推送信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("get-App-Upgrade")
	@ResponseBody
	public List<AppUpgrade> getAppUpgrade(HttpServletRequest request) {
		List<AppUpgrade> list = null;
		try {
			// 所有数据信息
			list = appUpgradeService.getAppUpgradeAll();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return list;
	}

	/**
	 * 删除APP升级信息
	 * 
	 * @param app
	 * @return
	 */
	@RequestMapping(value = "delete-App-Upgrade")
	@ResponseBody
	public JSONObject deleteAppUpgrade(@RequestBody AppUpgrade app) {
		JSONObject json = new JSONObject();
		try {
			int ret = appUpgradeService.deleteByPrimaryKey(app.getId());
			if (ret > 0) {
				json.put("code", 1);
				json.put("msg", "Delete Successfully!");
				json.put("data", null);
			} else {
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
	 * 编辑APP升级信息
	 * 
	 * @param app
	 * @return
	 */
	@RequestMapping(value = "edit-App-Upgrade")
	@ResponseBody
	public JSONObject editAppUpgrade(@RequestBody AppUpgrade app) {
		JSONObject json = new JSONObject();
		//添加状态是-1，此升级列表推送的是应用市场自身检测升级apk
		if(app.getType() == -1) {
			app.setCustomer("vs");
			// 查询数据是否存在
			AppUpgrade upgradeObj = appUpgradeService.selectByNameApkList(app);
			if (upgradeObj != null) {
				json.put("code", -1);
				json.put("msg", "The APP Upgrade list already exists, the list name is ["+upgradeObj.getName()+"].");
				json.put("data", null);
				return json;
			}
		}
		String upgradeName = app.getName();
		System.out.println("检测输入的APP升级信息名称: " + upgradeName);
		try {
			// 创建ip对象工具
			IPAddressUtils ip = new IPAddressUtils();
			ip.init();
			System.out.println(Util.getIpByteArrayFromString(upgradeName));
			// 如果格式为ip正确格式，说明输入的是ip
			if (Util.getIpByteArrayFromString(upgradeName) != null) {
				// 通过ip调用工具类获取所在的国家地区和供应商
				System.out.println("IP地址[" + upgradeName + "]获取到的区域信息:" + ip.getIPLocation(upgradeName).getCountry()
						+ ", 获取到的城市:" + ip.getIPLocation(upgradeName).getCity() + ", 运营商:"
						+ ip.getIPLocation(upgradeName).getArea());
				// 通过ip获取对应的国家地区
				String country = ip.getIPLocation(upgradeName).getCountry();
				app.setCustomer(country);
			}
			int ret = appUpgradeService.updateByPrimaryKeySelective(app);
			if (ret > 0) {
				json.put("code", 1);
				json.put("msg", "Successfully modified!");
				json.put("data", null);
			} else {
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
	 * 添加APP升级信息
	 * 
	 * @param app
	 * @return
	 */
	@RequestMapping(value = "add-App-Upgrade")
	@ResponseBody
	public JSONObject addAppUpgrade(@RequestBody AppUpgrade app) {
		JSONObject json = new JSONObject();
		//添加状态是-1，此升级列表推送的是应用市场自身检测升级apk
		if(app.getType() == -1) {
			app.setCustomer("vs");
			// 查询数据是否存在
			AppUpgrade upgradeObj = appUpgradeService.selectByNameApkList(app);
			if (upgradeObj != null) {
				json.put("code", -1);
				json.put("msg", "The APP Upgrade list already exists, the list name is ["+upgradeObj.getName()+"].");
				json.put("data", null);
				return json;
			}
			
		}
		String upgradeName = app.getName();
		System.out.println("检测输入的APP升级信息名称: " + upgradeName);
		try {
			// 创建ip对象工具
			IPAddressUtils ip = new IPAddressUtils();
			ip.init();
			System.out.println(Util.getIpByteArrayFromString(upgradeName));
			// 如果格式为ip正确格式，说明输入的是ip
			if (Util.getIpByteArrayFromString(upgradeName) != null) {
				// 通过ip调用工具类获取所在的国家地区和供应商
				System.out.println("IP地址[" + upgradeName + "]获取到的区域信息:" + ip.getIPLocation(upgradeName).getCountry()
						+ ", 获取到的城市:" + ip.getIPLocation(upgradeName).getCity() + ", 运营商:"
						+ ip.getIPLocation(upgradeName).getArea());
				// 通过ip获取对应的国家地区
				String country = ip.getIPLocation(upgradeName).getCountry();
				app.setCustomer(country);
			}
			int ret = appUpgradeService.insertSelective(app);
			if (ret > 0) {
				json.put("code", 1);
				json.put("msg", "Added successfully!");
				json.put("data", null);
			} else {
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
	 * 
	 * 检测是否输入的字符串是否输入的是IP地址
	 * 
	 * @param app
	 * @return
	 */
	@RequestMapping(value = "check-UpgradeName")
	@ResponseBody
	public JSONObject checkUpgradeName(@RequestBody AppUpgrade app) {
		JSONObject json = new JSONObject();
		String upgradeName = app.getName();
		System.out.println("检测输入的APP升级信息名称: " + app.getName());
		try {
			// 创建ip对象工具
			IPAddressUtils ip = new IPAddressUtils();
			ip.init();
			System.out.println(Util.getIpByteArrayFromString(upgradeName));
			// 如果格式为ip正确格式，说明输入的是ip
			if (Util.getIpByteArrayFromString(upgradeName) != null) {
				// 通过ip调用工具类获取所在的国家地区和供应商
				System.out.println("IP地址[" + upgradeName + "]获取到的区域信息:" + ip.getIPLocation(upgradeName).getCountry()
						+ ", 获取到的城市:" + ip.getIPLocation(upgradeName).getCity() + ", 运营商:"
						+ ip.getIPLocation(upgradeName).getArea());
				// 通过ip获取对应的国家地区
				String country = ip.getIPLocation(upgradeName).getCountry();
				app.setCustomer(country);
			}
			// 查询数据是否存在
			AppUpgrade apkUpgrade = appUpgradeService.selectByNameApkList(app);
			if (apkUpgrade != null) {
				json.put("code", 1);
				json.put("msg", "Ip "+app.getName()+" and ["+apkUpgrade.getName()+"] in the same area.");
				json.put("data", null);
			} else {
				json.put("code", 0);
				json.put("msg", "Data does not exist!");
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

	/***
	 * 
	 * 点击自动配置信息查看关联pak信息
	 * 
	 * @param addetailId
	 * @return
	 */
	@RequestMapping("appUpgrade-Detail")
	@ResponseBody
	public JSONObject appUpgradeDetail(@RequestBody JSONObject obj) {
		JSONObject json = new JSONObject();
		try {
			int id = Integer.parseInt(obj.getString("appUpgradeId"));
			// 获取推送的APP的信息
			AppUpgrade appUpgrade = appUpgradeService.selectByPrimaryKey(id);
			json.put("appUpgradeInfo", appUpgrade);
			System.out.println("apkid集合：" + appUpgrade.getApkidlist());
			// 获取所有上传的APP集合
			List<Appstore> listApp = appStoreService.getByAppstoreAll();

			// 判断没有apk推送
			if (listApp.size() == 0) {
				json.put("code", 1);
				json.put("msg", "No APP push information");
				json.put("data", null);
				return json;
			}
			// 未加入升级推送配置的APP集合
			List<ApkInfo> notAppUpgradeList = new ArrayList<ApkInfo>();

			// 已加入升级推送列表的APP集合
			List<ApkInfo> havAppUpgradeList = new ArrayList<ApkInfo>();

			// apkid集合字段
			String apkIdList = "";
			System.out.println(appUpgrade.getApkidlist());

			// 获取下拉框选项列表
			for (Appstore allApk : listApp) {
				// 初始化没有加入到升级列表中
				ApkInfo not = new ApkInfo();
				not.setApkid(allApk.getId());
				not.setApkName(allApk.getApkname());
				not.setPackageName(allApk.getApkpackagename());
				not.setVersionCode(Integer.valueOf(allApk.getApkversion()));
				not.setVersionName(allApk.getApkversionname());
				not.setUrl(allApk.getApkdownload());
				not.setSize(allApk.getApksize().toString());
				notAppUpgradeList.add(not);
			}

			// 不存在商城推送策略
			if (appUpgrade == null || appUpgrade.getApkidlist() == null || ("").equals(appUpgrade.getApkidlist())) {
				json.put("code", 1);
				json.put("msg", "Success");
				json.put("appUpgradeInfo", appUpgrade);
				json.put("havApkList", havAppUpgradeList);
				json.put("notApkList", notAppUpgradeList);
				Gson gson = new Gson();
				String str = gson.toJson(notAppUpgradeList);
				System.out.println("不存在商城推送策略，下拉列表apk：" + str);
				return json;
			}
			// 获取apkidlist的列
			apkIdList = appUpgrade.getApkidlist();
			// 拆开APP升级列表对应的apkid字段
			String[] arr = apkIdList.split(",");
			for (String string : arr) {
				Integer apkid = Integer.valueOf(string);
				System.out.println("APP升级拆开apkid查看：" + string);
				// 已经加入到升级列表中显示
				for (Appstore allApk : listApp) {
					if (allApk.getId().equals(apkid)) {
						// 初始化已经添加过的数据信息
						ApkInfo info = new ApkInfo();
						info.setApkid(allApk.getId());
						info.setApkName(allApk.getApkname());
						info.setPackageName(allApk.getApkpackagename());
						info.setVersionCode(Integer.valueOf(allApk.getApkversion()));
						info.setVersionName(allApk.getApkversionname());
						info.setUrl(allApk.getApkdownload());
						info.setSize(allApk.getApksize().toString());
						havAppUpgradeList.add(info);
						// 已添加的apk信息与下拉框选项列表对比相同，下拉框集合移除此pak
						Iterator<ApkInfo> it = notAppUpgradeList.iterator();
						while (it.hasNext()) {
							ApkInfo apk = it.next();
							if (apk.getApkid().equals(info.getApkid())) {
								it.remove();
							}
						}
					}
				}

			}

			json.put("code", 1);
			json.put("msg", "Success");
			json.put("appUpgradeInfo", appUpgrade);
			json.put("havApkList", havAppUpgradeList);
			json.put("notApkList", notAppUpgradeList);
			Gson gson = new Gson();
			String str = gson.toJson(notAppUpgradeList);
			System.out.println("下拉列表apk：" + str);
		} catch (NumberFormatException e) {
			json.put("code", -1);
			json.put("msg", "System Exception!");
			json.put("data", null);
			e.printStackTrace();
		}

		return json;
	}

	/**
	 * *删除关联apklist的信息列
	 * 
	 * @param app
	 * @return
	 */
	@Transactional
	@RequestMapping("delete-AppUpgradeList")
	@ResponseBody
	public JSONObject deleteAppUpgradeList(@RequestBody JSONObject obj) {
		JSONObject json = new JSONObject();
		String apkid = obj.getString("apkid");
		Integer appUpgradeId = Integer.valueOf(obj.getString("appUpgradeId"));
		try {
			AppUpgrade appUpgrade = appUpgradeService.selectByPrimaryKey(appUpgradeId);
			// apkid集合字段
			String apkIdList = appUpgrade.getApkidlist();
			// 重新整理apkid集合字段
			String newApkIdList = "";

			// 拆开APP升级列表对应的apkid字段
			String[] arr = apkIdList.split(",");
			for (String str : arr) {
				System.out.println("APP升级拆开apkid查看：" + str);
				if (str.equals(apkid))
					continue;
				newApkIdList += str + ",";
			}
			appUpgrade.setId(appUpgradeId);
			appUpgrade.setApkidlist(newApkIdList);
			int ret = appUpgradeService.updateByPrimaryKeySelective(appUpgrade);
			if (ret > 0) {
				json.put("code", 1);
				json.put("msg", "Delete Successfully!");
				json.put("data", null);
			} else {
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
	 * *添加app升级安装列的apkidlist
	 * 
	 * @param app
	 * @return
	 */
	@Transactional
	@RequestMapping("add-AppUpgradeList")
	@ResponseBody
	public JSONObject addAppUpgradeList(@RequestBody JSONObject obj) {
		JSONObject json = new JSONObject();
		String apkid = obj.getString("apkid");
		Integer appUpgradeId = Integer.valueOf(obj.getString("appUpgradeId"));
		try {
			AppUpgrade appUpgrade = appUpgradeService.selectByPrimaryKey(appUpgradeId);
			// apkid集合字段
			String apkIdList = appUpgrade.getApkidlist();
			// 重新整理apkid集合字段
			if (apkIdList == null) {
				apkIdList = "";
			}
			// 拆开APP升级列表对应的apkid字段
			String[] arr = apkid.split(",");
			for (String strid : arr) {
				apkIdList += strid + ",";
			}
			appUpgrade.setId(appUpgradeId);
			appUpgrade.setApkidlist(apkIdList);
			int ret = appUpgradeService.updateByPrimaryKeySelective(appUpgrade);
			if (ret > 0) {
				json.put("code", 1);
				json.put("msg", "Add Successfully!");
				json.put("data", null);
			} else {
				json.put("code", 0);
				json.put("msg", "Add Failed!");
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
	 * *商城APP配置，查询所有APP商城推送的列表，商城下拉框选项
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("check-AppStoreList")
	@ResponseBody
	public List<AppUpgrade> checkAppStoreList(HttpServletRequest request) {
		List<AppUpgrade> list = null;
		try {
			// 所有数据信息
			list = appUpgradeService.selectByStoreApkList();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return list;
	}

	/***
	 * *自动、静默安装配置，查询所有APP商城推送的列表，商城下拉框选项
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("check-Auto-Static-AppNameList")
	@ResponseBody
	public List<AppUpgrade> checkAutoStaticAppNameList(HttpServletRequest request) {
		List<AppUpgrade> list = null;
		try {
			// 所有数据信息
			list = appUpgradeService.selectByAutoStaticApkList();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return list;
	}

}
