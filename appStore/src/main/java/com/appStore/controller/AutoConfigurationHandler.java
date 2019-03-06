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
import com.appStore.entity.ApkAutomaticConfiguration;
import com.appStore.entity.ApkInfo;
import com.appStore.entity.ApkStoreConfiguration;
import com.appStore.entity.AppUpgrade;
import com.appStore.entity.Appstore;
import com.appStore.entity.AutoInfo;
import com.appStore.entity.ModelVersion;
import com.appStore.service.ApkAutoConfigurationService;
import com.appStore.service.AppStoreService;
import com.appStore.service.AppUpgradeService;
import com.appStore.service.ModelVersionService;
import com.google.gson.Gson;

@Controller
public class AutoConfigurationHandler {
	@Autowired
	private ApkAutoConfigurationService autoConfigurationService;
	@Autowired
	private ModelVersionService modelVersionService;
	@Autowired
	private AppUpgradeService appUpgradeService;
	@Autowired
	private AppStoreService appStoreService;

	/**
	 * 查询所有静默安装信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("getAutoConfiguration")
	@ResponseBody
	public List<AutoInfo> getAutomaticConfiguration(HttpServletRequest request) {
		// 显示详情信息对象集合
		List<AutoInfo> listInfo = new ArrayList<AutoInfo>();
		try {
			// 查询关联配置表数据信息集合
			List<ApkAutomaticConfiguration> list = autoConfigurationService.selectByApkAutoList();
			if (list.size() != 0) {
				for (ApkAutomaticConfiguration apkConfiguration : list) {
					Integer modelid = apkConfiguration.getModelVersionId();
					Integer appid = apkConfiguration.getAppUpgradeId();
					// 初始化显示详情信息对象
					AutoInfo info = new AutoInfo();
					info.setId(apkConfiguration.getId());
					info.setStatus(apkConfiguration.getStatus());
					info.setModelVersionId(modelid);
					info.setAppUpgradeId(appid);
					// 查询机型版本详情信息
					ModelVersion modelVersion = modelVersionService.selectByPrimaryKey(modelid);
					if(modelVersion != null) {
						info.setModel(modelVersion.getModel());
						info.setAndroid(modelVersion.getAndroid());
						info.setCustomer(modelVersion.getCustomer());
						info.setDevice(modelVersion.getDevice());
						listInfo.add(info);
					}
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return listInfo;
	}

	/**
	 * 删除自动配置信息
	 * 
	 * @param auto
	 * @return
	 */
	@RequestMapping(value = "delete-AutoConfiguration")
	@ResponseBody
	public JSONObject deleteAutoConfiguration(@RequestBody ApkAutomaticConfiguration auto) {
		JSONObject json = new JSONObject();
		try {
			int ret = autoConfigurationService.deleteByPrimaryKey(auto.getId());
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
	 * 编辑自动配置信息
	 * 
	 * @param auto
	 * @return
	 */
	@RequestMapping(value = "update-AutoConfiguration")
	@ResponseBody
	public JSONObject editAutoConfiguration(@RequestBody ApkAutomaticConfiguration auto) {
		JSONObject json = new JSONObject();
		try {
			int ret = autoConfigurationService.updateByPrimaryKeySelective(auto);
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
	 * *检测配置信息是否存在重复的机型版本
	 * @param auto
	 * @return
	 */
	@RequestMapping(value = "check-AutoConfiguration")
	@ResponseBody
	public JSONObject checkStoreConfiguration(@RequestBody ApkAutomaticConfiguration auto) {
		JSONObject json = new JSONObject();
		try {
			ApkAutomaticConfiguration apkobj = autoConfigurationService.selectByApkAutoStatus(auto.getModelVersionId());
			if(apkobj == null) {
				json.put("code", 1);
				json.put("msg", "Does not exist!");
				json.put("data", null);
			}else {
				json.put("code", 0);
				json.put("msg", "This model version configuration information already exists!");
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
	 * 添加自动配置信息
	 * 
	 * @param auto
	 * @return
	 */
	@RequestMapping(value = "add-AutoConfiguration")
	@ResponseBody
	public JSONObject addAutoConfiguration(@RequestBody ApkAutomaticConfiguration auto) {
		JSONObject json = new JSONObject();
		try {
			int ret = autoConfigurationService.insertSelective(auto);
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

	/***
	 * 
	 * 点击自动配置信息查看关联pak信息
	 * 
	 * @param addetailId
	 * @return
	 */
	@RequestMapping("auto-detail")
	@ResponseBody
	public JSONObject autoDetail(@RequestBody JSONObject obj) {
		JSONObject json = new JSONObject();
		try {
			int id = Integer.parseInt(obj.getString("autoId"));
			// 获取匹配表关联对象
			ApkAutomaticConfiguration configuration = autoConfigurationService.selectByPrimaryKey(id);
			// 查询机型版本详情信息
			ModelVersion mv = modelVersionService.selectByPrimaryKey(configuration.getModelVersionId());
			// 获取推送的APP的信息
			AppUpgrade appUpgrade = appUpgradeService.selectByPrimaryKey(configuration.getAppUpgradeId());
			// 显示机型版本信息
			json.put("modelVersion", mv);
			// 已加入升级推送列表的APP集合
			List<ApkInfo> havAppUpgradeList = new ArrayList<ApkInfo>();
			// 不存在商城推送策略
			if (appUpgrade == null || appUpgrade.getApkidlist() == null || ("").equals(appUpgrade.getApkidlist())) {
				json.put("code", 1);
				json.put("msg", "No added configuration apk information.");
				json.put("havApkList", havAppUpgradeList);
				System.out.println("自动配置不存在商城推送策略");
				return json;
			}
			// 获取所有上传的APP集合
			List<Appstore> listApp = appStoreService.getByAppstoreAll();

			// 判断没有apk推送
			if (listApp.size() == 0) {
				json.put("code", 1);
				json.put("msg", "No APP push information");
				json.put("data", null);
				return json;
			}
			// 获取apkidlist的列
			String apkIdList = appUpgrade.getApkidlist();
			// 拆开APP升级列表对应的apkid字段
			String[] arr = apkIdList.split(",");
			for (String string : arr) {
				Integer apkid = Integer.valueOf(string);
				System.out.println("APP升级拆开apkid查看：" + string);

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
					}

				}
			}

			json.put("code", 1);
			json.put("msg", "Success");
			json.put("auotApkList", havAppUpgradeList);
			Gson gson = new Gson();
			String str = gson.toJson(havAppUpgradeList);
			System.out.println("自动配置获取apk显示列表：" + str);
		} catch (NumberFormatException e) {
			json.put("code", -1);
			json.put("msg", "System Exception!");
			json.put("data", null);
			e.printStackTrace();
		}

		return json;
	}

}
