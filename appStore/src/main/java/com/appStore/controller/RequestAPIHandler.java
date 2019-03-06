package com.appStore.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.appStore.entity.ApkAutomaticConfiguration;
import com.appStore.entity.ApkInfo;
import com.appStore.entity.ApkStaticConfiguration;
import com.appStore.entity.ApkStoreConfiguration;
import com.appStore.entity.AppStoreUpgrade;
import com.appStore.entity.AppType;
import com.appStore.entity.AppUpgrade;
import com.appStore.entity.Appstore;
import com.appStore.entity.Box;
import com.appStore.entity.Filtering;
import com.appStore.entity.ModelVersion;
import com.appStore.entity.XmlFile;
import com.appStore.service.ApkAutoConfigurationService;
import com.appStore.service.ApkStaticConfigurationService;
import com.appStore.service.ApkStoreConfigurationService;
import com.appStore.service.AppStoreService;
import com.appStore.service.AppTypeService;
import com.appStore.service.AppUpgradeService;
import com.appStore.service.BoxService;
import com.appStore.service.FilteringService;
import com.appStore.service.ModelVersionService;
import com.appStore.service.XmlFileService;
import com.appStore.service.XmlFilterService;
import com.appStore.tools.IPAddressUtils;
import com.appStore.tools.LanguageUtil;
import com.appStore.tools.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sf.json.JSONObject;

@Controller
public class RequestAPIHandler {
	@Autowired
	private FilteringService filteringService;
	@Autowired
	private ApkAutoConfigurationService autoConfigurationService;
	@Autowired
	private ApkStaticConfigurationService staticConfigurationService;
	@Autowired
	private BoxService boxService;
	@Autowired
	private AppStoreService appStoreService;
	@Autowired
	private XmlFileService xmlFileService;

	@Autowired
	private XmlFilterService xmlFilterService;
	@Autowired
	private AppTypeService appTypeService;
	@Autowired
	private ModelVersionService modelVersionService;
	@Autowired
	private AppUpgradeService appUpgradeService;

	@Autowired
	private ApkStoreConfigurationService apkStoreConfigurationService;

	/***
	 * 自动配置安装apk信息
	 * 
	 * @param jsonRec
	 * @return
	 * @throws UnknownHostException
	 */
	@RequestMapping(value = "/autoConfiguration", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject autoRecord(HttpServletRequest jsonRec) throws UnknownHostException {
		JSONObject json = new JSONObject();
		// 终端以post方式传递json字符串，获取数据流
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(jsonRec.getInputStream(), "UTF-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line = null;
		StringBuilder sb = new StringBuilder();
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String objectStr = sb.toString();
		System.out.println("自动安装获取终端数据：" + objectStr);
		// Json字符串转Object
		Map<Object, Object> map = new Gson().fromJson(objectStr, Map.class);

		// CPUsn序列号
		String CPUSerialNo = map.get("CPUSerialNo").toString();
		String Language = map.get("Language").toString();
		String DeviceDpi = map.get("DeviceDpi").toString();
		String ProductName = map.get("ProductName").toString();

		String Factory = map.get("Factory").toString();
		String Country = map.get("Country").toString();
		// 客户名称
		String Custom = map.get("Custom").toString();
		String Brand = map.get("Brand").toString();
		// 获取终端访问的IP地址
		String IpAddress = getIpAddr(jsonRec);
		// 设备机型
		String Model = map.get("Model").toString();
		String Hardware = map.get("Hardware").toString();
		// 串口Sn序列号
		String Serials = map.get("Serials").toString();

		String TotalMemory = map.get("TotalMemory").toString();
		String Board = map.get("Board").toString();
		String Fingerpint = map.get("Fingerpint").toString();
		// Mac地址
		String MacAddress = map.get("MacAddress").toString();
		String Type = map.get("Type").toString();

		String Device = map.get("Device").toString();
		String BuildTime = map.get("BuildTime").toString();
		// 日期字符串转换成时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(BuildTime);
			System.out.println(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String DeviceVersionCode = map.get("DeviceVersionCode").toString();
		String ScreenDensity = map.get("ScreenDensity").toString();
		String CPUName = map.get("CPUName").toString();

		String FreeMemory = map.get("FreeMemory").toString();
		// Android版本号
		String AndroidVersion = map.get("AndroidVersion").toString();
		System.out.println("终端传来的Mac：  " + MacAddress);

		// 数据填充
		Box box = new Box();
		box.setMac(MacAddress);
		box.setSn(Serials);
		box.setLanguage(Language);
		box.setDevicedpi(DeviceDpi);
		box.setProductname(ProductName);
		box.setFactory(Factory);
		box.setCustomer(Custom);
		box.setBrand(Brand);
		box.setLastLoginIp(IpAddress);
		box.setModel(Model);
		box.setHardwareversion(Hardware);
		box.setCpuserialno(CPUSerialNo);
		box.setTotalmemory(TotalMemory);
		box.setBoard(Board);
		box.setFingerpint(Fingerpint);
		box.setBoxtype(Type);
		box.setDevice(Device);
		box.setBuildtime(date);
		box.setDeviceVersionCode(DeviceVersionCode);
		box.setScreendensity(ScreenDensity);
		box.setCpuName(CPUName);
		box.setFreememory(FreeMemory);
		box.setBuildversion(AndroidVersion);
		box.setAndroidVersion(AndroidVersion);
		box.setOnlinetime(new Date());
		box.setCountry(Country);
		box.setCreatetime(new Date());
		try {
			// 判断盒子是否访问过
			if (boxService.selectMacSn(MacAddress, Serials) != null) {
				boxService.updateByPrimaryKeySelective(box);
			} else {
				boxService.insertSelective(box);
			}
		} catch (Exception e) {
			json.put("code", -1);
			json.put("msg", "Device entry failed");
			json.put("data", null);
			e.printStackTrace();
		}
		// 初始化设备黑白名单比较信息
		Filtering record = new Filtering();
		record.setModel(Model);
		record.setSn(Serials);
		record.setMac(MacAddress);
		// 返回设备黑白名单状态
		int status = filteringService.getBWStatus(record);
		// -1加入黑名单，拦截无法继续访问
		if (status == -1) {
			System.out.println("自动安装黑名单拦截Mac：" + MacAddress);
			json.put("code", -1);
			json.put("msg", "Device has been blacklisted.");
			json.put("data", null);
		}
		// 1加入白名单，继续访问
		else if (status == 1) {
			try {
				// 获取所有所有上传的APP集合
				List<Appstore> listApp = appStoreService.getByAppstoreAll();
				// 判断没有apk安装包
				if (listApp.size() == 0) {
					json.put("code", 1);
					json.put("msg", "No apk install package push.");
					json.put("data", null);
					return json;
				}
				ModelVersion mv = new ModelVersion();
				mv.setModel(Model);
				mv.setAndroid(AndroidVersion);
				mv.setCustomer(Custom);
				ModelVersion modelVersion = modelVersionService.selectByModelVersionStatus(mv);
				// 查看配置信息
				ApkAutomaticConfiguration apkobj = null;
				// 创建配置安装的apk集合对象
				List<ApkInfo> apkInfoList = new ArrayList<ApkInfo>();
				int myret = 1;
				// 没有匹配的机型版本
				if (modelVersion == null) {
					myret = -1;
				} else {
					Integer mvid = modelVersion.getId();
					apkobj = autoConfigurationService.selectByApkAutoStatus(mvid);
				}
				// 查看配置信息,没有配置推送
				if (apkobj == null) {
					myret = -1;
				}
				// 没有对应的机型版本或者是没有推送配置，默认按照ip获取所在区域推送
				if (myret == -1) {
					// 创建ip对象工具
					IPAddressUtils ip = new IPAddressUtils();
					ip.init();
					System.out.println(Util.getIpByteArrayFromString(IpAddress));
					if (Util.getIpByteArrayFromString(IpAddress) != null) {
						// 通过ip调用工具类获取所在的国家地区和供应商
						System.out.println("IP地址[" + IpAddress + "]获取到的区域信息:" + ip.getIPLocation(IpAddress).getCountry()
								+ ", 获取到的城市:" + ip.getIPLocation(IpAddress).getCity() + ", 运营商:"
								+ ip.getIPLocation(IpAddress).getArea());
						// 通过ip获取对应的国家地区
						String country = ip.getIPLocation(IpAddress).getCountry();
						// 初始化查询条件
						AppUpgrade recordApp = new AppUpgrade();
						recordApp.setType(0);
						recordApp.setCustomer(country);
						AppUpgrade apkUpgrade = appUpgradeService.selectByNameApkList(recordApp);
						if (apkUpgrade != null) {
							String apkListString = apkUpgrade.getApkidlist();
							if (!(apkListString == null || ("").equals(apkListString))) {
								// 拆开APP升级列表对应的apkid字段
								String[] arr = apkListString.split(",");
								for (String strid : arr) {
									Integer apkid = Integer.valueOf(strid);
									System.out.println("拆开apkid查看：" + strid);
									for (Appstore apkObj : listApp) {
										if (apkObj.getId().equals(apkid)) {
											// 初始化配置安装的apk对象
											ApkInfo newApkInfo = new ApkInfo();
											newApkInfo.setApkid(apkObj.getId());
											newApkInfo.setApkName(apkObj.getApkname());
											newApkInfo.setPackageName(apkObj.getApkpackagename());
											newApkInfo.setVersionCode(Integer.valueOf(apkObj.getApkversion()));
											newApkInfo.setVersionName(apkObj.getApkversionname());
											newApkInfo.setUrl(apkObj.getApkdownload());
											newApkInfo.setSize(apkObj.getApksize().toString());
											apkInfoList.add(newApkInfo);
											// pak下载次数累计
											appStoreService.updateForDownloadTotal(apkObj.getId());
										}
									}
								}
							}
						}
					}
				} else {
					// apkid集合字段
					String apkIdList = "";
					// 状态为1，匹配通过
					if (apkobj.getStatus() == 1) {
						// 获取配置关联apk列表对象
						AppUpgrade appUpgrade = appUpgradeService.selectByPrimaryKey(apkobj.getAppUpgradeId());
						// 不存在app配置推送策略
						if (appUpgrade == null || appUpgrade.getApkidlist() == null
								|| ("").equals(appUpgrade.getApkidlist())) {
							json.put("code", 1);
							json.put("msg", "No app found for configuration installation.");
							json.put("data", null);
							return json;
						}
						// 商城存在添加上传apk的策略的情况，
						else {
							apkIdList = appUpgrade.getApkidlist();
						}

						// 拆开APP升级列表对应的apkid字段
						String[] arr = apkIdList.split(",");
						for (String strid : arr) {
							Integer apkid = Integer.valueOf(strid);
							System.out.println("自动配置拆开apkid查看：" + strid);
							for (Appstore apkObj : listApp) {
								if (apkObj.getId().equals(apkid)) {
									// 初始化配置安装的apk对象
									ApkInfo newApkInfo = new ApkInfo();
									newApkInfo.setApkid(apkObj.getId());
									newApkInfo.setApkName(apkObj.getApkname());
									newApkInfo.setPackageName(apkObj.getApkpackagename());
									newApkInfo.setVersionCode(Integer.valueOf(apkObj.getApkversion()));
									newApkInfo.setVersionName(apkObj.getApkversionname());
									newApkInfo.setUrl(apkObj.getApkdownload());
									newApkInfo.setSize(apkObj.getApksize().toString());
									apkInfoList.add(newApkInfo);
									// pak下载次数累计
									appStoreService.updateForDownloadTotal(apkObj.getId());
								}
							}
						}
					}
				}
				// 集合对象转换json对象
				Gson gson = new Gson();
				String str = gson.toJson(apkInfoList);
				System.out.println("自动集合转换成json字符串对象：" + str);
				json.put("code", 1);
				json.put("msg", "success");
				json.put("data", str);
			} catch (Exception e) {
				json.put("code", -1);
				json.put("msg", "Automatic configuration data is failed");
				json.put("data", null);
				e.printStackTrace();
			}
		} else {
			json.put("code", -1);
			json.put("msg", "An exception occurred in the black and white list");
			json.put("data", null);
		}
		return json;

	}

	/***
	 * 静默安装apk信息
	 * 
	 * @param jsonRec
	 * @return
	 * @throws UnknownHostException
	 */
	@RequestMapping(value = "/staticConfiguration", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject staticRecord(HttpServletRequest jsonRec) throws UnknownHostException {
		JSONObject json = new JSONObject();
		// 终端以post方式传递json字符串，获取数据流
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(jsonRec.getInputStream(), "UTF-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line = null;
		StringBuilder sb = new StringBuilder();
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String objectStr = sb.toString();
		System.out.println("静默安装获取终端设备基本信息：" + objectStr);
		// Json字符串转Object
		Map<Object, Object> map = new Gson().fromJson(objectStr, Map.class);

		// CPUsn序列号
		String CPUSerialNo = map.get("CPUSerialNo").toString();
		String Language = map.get("Language").toString();
		String DeviceDpi = map.get("DeviceDpi").toString();
		String ProductName = map.get("ProductName").toString();

		String Factory = map.get("Factory").toString();
		String Country = map.get("Country").toString();
		// 客户名称
		String Custom = map.get("Custom").toString();
		String Brand = map.get("Brand").toString();

		// 获取终端访问的IP地址
		String IpAddress = getIpAddr(jsonRec);
		// 设备机型
		String Model = map.get("Model").toString();
		String Hardware = map.get("Hardware").toString();
		// 串口SN序列号
		String Serials = map.get("Serials").toString();

		String TotalMemory = map.get("TotalMemory").toString();
		String Board = map.get("Board").toString();
		String Fingerpint = map.get("Fingerpint").toString();
		// Mac地址
		String MacAddress = map.get("MacAddress").toString();
		String Type = map.get("Type").toString();

		String Device = map.get("Device").toString();
		String BuildTime = map.get("BuildTime").toString();
		// 日期字符串转换成时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(BuildTime);
			System.out.println(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String DeviceVersionCode = map.get("DeviceVersionCode").toString();
		String ScreenDensity = map.get("ScreenDensity").toString();
		String CPUName = map.get("CPUName").toString();

		String FreeMemory = map.get("FreeMemory").toString();
		// Android版本号
		String AndroidVersion = map.get("AndroidVersion").toString();
		System.out.println("终端传来的Mac：  " + MacAddress);

		// 数据填充
		Box box = new Box();
		box.setMac(MacAddress);
		box.setSn(Serials);
		box.setLanguage(Language);
		box.setDevicedpi(DeviceDpi);
		box.setProductname(ProductName);
		box.setFactory(Factory);
		box.setCustomer(Custom);
		box.setBrand(Brand);
		box.setLastLoginIp(IpAddress);
		box.setModel(Model);
		box.setHardwareversion(Hardware);
		box.setCpuserialno(CPUSerialNo);
		box.setTotalmemory(TotalMemory);
		box.setBoard(Board);
		box.setFingerpint(Fingerpint);
		box.setBoxtype(Type);
		box.setDevice(Device);
		box.setBuildtime(date);
		box.setDeviceVersionCode(DeviceVersionCode);
		box.setScreendensity(ScreenDensity);
		box.setCpuName(CPUName);
		box.setFreememory(FreeMemory);
		box.setBuildversion(AndroidVersion);
		box.setAndroidVersion(AndroidVersion);
		box.setOnlinetime(new Date());
		box.setCountry(Country);
		box.setCreatetime(new Date());
		try {
			// 判断盒子是否访问过
			if (boxService.selectMacSn(MacAddress, Serials) != null) {
				boxService.updateByPrimaryKeySelective(box);
			} else {
				boxService.insertSelective(box);
			}
		} catch (Exception e) {
			json.put("code", -1);
			json.put("msg", "Device entry failed");
			json.put("data", null);
			e.printStackTrace();
		}

		// 初始化设备黑白名单比较信息
		Filtering record = new Filtering();
		record.setModel(Model);
		record.setSn(Serials);
		record.setMac(MacAddress);
		// 返回设备黑白名单状态
		int status = filteringService.getBWStatus(record);
		// -1加入黑名单，拦截无法继续访问
		if (status == -1) {
			System.out.println("静默安装黑名单拦截Mac：" + MacAddress);
			json.put("code", -1);
			json.put("msg", "Device has been blacklisted.");
			json.put("data", null);
		}
		// 1加入白名单，继续访问
		else if (status == 1) {
			try {
				// 获取所有所有上传的APP集合
				List<Appstore> listApp = appStoreService.getByAppstoreAll();
				// 判断没有apk安装包
				if (listApp.size() == 0) {
					json.put("code", 1);
					json.put("msg", "No apk install package push.");
					json.put("data", null);
					return json;
				}
				ModelVersion mv = new ModelVersion();
				mv.setModel(Model);
				mv.setAndroid(AndroidVersion);
				mv.setCustomer(Custom);
				ModelVersion modelVersion = modelVersionService.selectByModelVersionStatus(mv);
				// 查看配置信息
				ApkStaticConfiguration apkobj = null;
				// 创建配置安装的apk集合对象
				List<ApkInfo> apkInfoList = new ArrayList<ApkInfo>();
				int myret = 1;
				// 没有匹配的机型版本
				if (modelVersion == null) {
					myret = -1;
				} else {
					Integer mvid = modelVersion.getId();
					apkobj = staticConfigurationService.selectByApkStaticStatus(mvid);
				}
				// 查看配置信息,没有配置推送
				if (apkobj == null) {
					myret = -1;
				}
				// 没有对应的机型版本或者是没有推送配置，默认按照ip获取所在区域推送
				if (myret == -1) {
					// 创建ip对象工具
					IPAddressUtils ip = new IPAddressUtils();
					ip.init();
					System.out.println(Util.getIpByteArrayFromString(IpAddress));
					if (Util.getIpByteArrayFromString(IpAddress) != null) {
						// 通过ip调用工具类获取所在的国家地区和供应商
						System.out.println("IP地址[" + IpAddress + "]获取到的区域信息:" + ip.getIPLocation(IpAddress).getCountry()
								+ ", 获取到的城市:" + ip.getIPLocation(IpAddress).getCity() + ", 运营商:"
								+ ip.getIPLocation(IpAddress).getArea());
						// 通过ip获取对应的国家地区
						String country = ip.getIPLocation(IpAddress).getCountry();
						// 初始化查询条件
						AppUpgrade recordApp = new AppUpgrade();
						recordApp.setType(0);
						recordApp.setCustomer(country);
						AppUpgrade apkUpgrade = appUpgradeService.selectByNameApkList(recordApp);
						if (apkUpgrade != null) {
							String apkListString = apkUpgrade.getApkidlist();
							if (!(apkListString == null || ("").equals(apkListString))) {
								// 拆开APP升级列表对应的apkid字段
								String[] arr = apkListString.split(",");
								for (String strid : arr) {
									Integer apkid = Integer.valueOf(strid);
									System.out.println("拆开apkid查看：" + strid);
									for (Appstore apkObj : listApp) {
										if (apkObj.getId().equals(apkid)) {
											ApkInfo newApkInfo = new ApkInfo();
											// 初始化静默安装集合对象
											newApkInfo.setApkid(apkObj.getId());
											newApkInfo.setApkName(apkObj.getApkname());
											newApkInfo.setPackageName(apkObj.getApkpackagename());
											newApkInfo.setVersionCode(Integer.valueOf(apkObj.getApkversion()));
											newApkInfo.setVersionName(apkObj.getApkversionname());
											newApkInfo.setUrl(apkObj.getApkdownload());
											newApkInfo.setSize(apkObj.getApksize().toString());
											apkInfoList.add(newApkInfo);
										}
									}
								}
							}
						}
					}
				} else {
					// apkid集合字段
					String apkIdList = "";
					// 过滤状态为1，匹配通过
					if (apkobj.getStatus() == 1) {
						// 获取配置关联apk列表对象
						AppUpgrade appUpgrade = appUpgradeService.selectByPrimaryKey(apkobj.getAppUpgradeId());
						// 不存在app配置推送策略
						if (appUpgrade == null || appUpgrade.getApkidlist() == null
								|| ("").equals(appUpgrade.getApkidlist())) {
							json.put("code", 1);
							json.put("msg", "No app found for configuration installation.");
							json.put("data", null);
							return json;
						}
						// 商城存在添加上传apk的策略的情况，
						else {
							apkIdList = appUpgrade.getApkidlist();
						}

						// 拆开APP升级列表对应的apkid字段
						String[] arr = apkIdList.split(",");
						for (String strid : arr) {
							Integer apkid = Integer.valueOf(strid);
							System.out.println("静默安装拆开apkid查看：" + strid);
							for (Appstore apkObj : listApp) {
								if (apkObj.getId().equals(apkid)) {
									ApkInfo newApkInfo = new ApkInfo();
									// 初始化静默安装集合对象
									newApkInfo.setApkid(apkObj.getId());
									newApkInfo.setApkName(apkObj.getApkname());
									newApkInfo.setPackageName(apkObj.getApkpackagename());
									newApkInfo.setVersionCode(Integer.valueOf(apkObj.getApkversion()));
									newApkInfo.setVersionName(apkObj.getApkversionname());
									newApkInfo.setUrl(apkObj.getApkdownload());
									newApkInfo.setSize(apkObj.getApksize().toString());
									apkInfoList.add(newApkInfo);
								}
							}
						}
					}
				}

				// 存在静默安装对象集合的情况，apk版本升级比较
				if (apkInfoList.size() != 0) {
					// 获取终端apk的jsonList缓存信息
					Gson gson1 = new Gson();
					String jsonList = gson1.toJson(map.get("APPList"));
					List<ApkInfo> list = gson1.fromJson(jsonList, new TypeToken<List<ApkInfo>>() {
					}.getType());
					System.out.println("-------获取的终端pak缓存信息-------" + list);
					Iterator<ApkInfo> it = apkInfoList.iterator();
					while (it.hasNext()) {
						ApkInfo apk = it.next();

						// 存在静默安装缓存文件，需要对比升级安装
						if (list != null) {
							for (ApkInfo person1 : list) {
								// apk名称
								String apkName = person1.getApkName();
								// apk包名
								String packageName = person1.getPackageName();
								// apk版本号
								int versionCode = person1.getVersionCode();
								// 比较apk名称相同，版本比已安装的相同或更低，从推送集合中移除掉
								if (apk.getApkName().equals(apkName) && apk.getPackageName().equals(packageName)
										&& Integer.valueOf(apk.getVersionCode()) <= versionCode) {
									it.remove();
									continue;
								} else {
									// pak下载次数累计
									appStoreService.updateForDownloadTotal(apk.getApkid());
								}
							}
						} else {
							// pak下载次数累计
							appStoreService.updateForDownloadTotal(apk.getApkid());
						}
					}

				}

				// 集合对象转换json对象
				Gson gson = new Gson();
				String str = gson.toJson(apkInfoList);
				System.out.println("静默安装集合转换成json字符串对象：" + str);
				json.put("code", 1);
				json.put("msg", "success");
				json.put("data", str);
			} catch (Exception e) {
				json.put("code", -1);
				json.put("msg", "Silent installation data is failed");
				json.put("data", null);
				e.printStackTrace();
			}
		} else {
			json.put("code", -1);
			json.put("msg", "An exception occurred in the black and white list");
			json.put("data", null);
		}

		return json;

	}

	/***
	 * 终端商城APP集合获取信息
	 * 
	 * @param jsonRec
	 * @return
	 * @throws UnknownHostException
	 */
	@RequestMapping(value = "/storeAppList", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject storeRecord(HttpServletRequest jsonRec) throws UnknownHostException {
		JSONObject json = new JSONObject();
		// 终端以post方式传递json字符串，获取数据流
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(jsonRec.getInputStream(), "UTF-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line = null;
		StringBuilder sb = new StringBuilder();
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String objectStr = sb.toString();
		System.out.println("商城列表安装获取终端数据：" + objectStr);
		// Json字符串转Object
		Map<Object, Object> map = new Gson().fromJson(objectStr, Map.class);

		// CPUsn序列号
		String CPUSerialNo = map.get("CPUSerialNo").toString();
		String Language = map.get("Language").toString();
		String DeviceDpi = map.get("DeviceDpi").toString();
		String ProductName = map.get("ProductName").toString();

		String Factory = map.get("Factory").toString();
		String Country = map.get("Country").toString();
		// 客户名称
		String Custom = map.get("Custom").toString();
		String Brand = map.get("Brand").toString();

		// 获取终端访问的IP地址
		String IpAddress = getIpAddr(jsonRec);
		// 设备机型
		String Model = map.get("Model").toString();
		String Hardware = map.get("Hardware").toString();
		String Serials = map.get("Serials").toString();

		String TotalMemory = map.get("TotalMemory").toString();
		String Board = map.get("Board").toString();
		String Fingerpint = map.get("Fingerpint").toString();
		// Mac地址
		String MacAddress = map.get("MacAddress").toString();
		String Type = map.get("Type").toString();

		String Device = map.get("Device").toString();
		String BuildTime = map.get("BuildTime").toString();
		// 日期字符串转换成时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(BuildTime);
			System.out.println(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String DeviceVersionCode = map.get("DeviceVersionCode").toString();
		String ScreenDensity = map.get("ScreenDensity").toString();
		String CPUName = map.get("CPUName").toString();

		String FreeMemory = map.get("FreeMemory").toString();
		// Android版本号
		String AndroidVersion = map.get("AndroidVersion").toString();
		System.out.println("终端传来的Mac：  " + MacAddress);

		// 数据填充
		Box box = new Box();
		box.setMac(MacAddress);
		box.setSn(Serials);
		box.setLanguage(Language);
		box.setDevicedpi(DeviceDpi);
		box.setProductname(ProductName);
		box.setFactory(Factory);
		box.setCustomer(Custom);
		box.setBrand(Brand);
		box.setLastLoginIp(IpAddress);
		box.setModel(Model);
		box.setHardwareversion(Hardware);
		box.setCpuserialno(CPUSerialNo);
		box.setTotalmemory(TotalMemory);
		box.setBoard(Board);
		box.setFingerpint(Fingerpint);
		box.setBoxtype(Type);
		box.setDevice(Device);
		box.setBuildtime(date);
		box.setDeviceVersionCode(DeviceVersionCode);
		box.setScreendensity(ScreenDensity);
		box.setCpuName(CPUName);
		box.setFreememory(FreeMemory);
		box.setBuildversion(AndroidVersion);
		box.setAndroidVersion(AndroidVersion);
		box.setOnlinetime(new Date());
		box.setCountry(Country);
		box.setCreatetime(new Date());
		try {
			// 判断盒子是否访问过
			if (boxService.selectMacSn(MacAddress, Serials) != null) {
				boxService.updateByPrimaryKeySelective(box);
			} else {
				boxService.insertSelective(box);
			}
		} catch (Exception e) {
			json.put("code", -1);
			json.put("msg", "Device entry failed");
			json.put("data", null);
			e.printStackTrace();
		}

		// 初始化设备黑白名单比较信息
		Filtering record = new Filtering();
		record.setModel(Model);
		record.setSn(Serials);
		record.setMac(MacAddress);
		// 返回设备黑白名单状态
		int status = filteringService.getBWStatus(record);
		// 获取商城推送的APP集合
		List<AppStoreUpgrade> storeUpgradelist = new ArrayList<AppStoreUpgrade>();
		// -1加入黑名单，拦截无法继续访问
		if (status == -1) {
			System.out.println("终端商城黑名单拦截Mac：" + MacAddress);
			json.put("code", -1);
			json.put("msg", "Device has been blacklisted.");
			json.put("data", null);
		}
		// 1加入白名单，继续访问
		else if (status == 1) {
			try {
				// 获取所有所有上传的APP集合
				List<Appstore> listApp = appStoreService.getByAppstoreAll();
				// 判断没有apk
				if (listApp.size() == 0) {
					json.put("code", 1);
					json.put("msg", "No mall APP push message.");
					json.put("data", null);
					return json;
				}
				ModelVersion mv = new ModelVersion();
				mv.setModel(Model);
				mv.setAndroid(AndroidVersion);
				mv.setCustomer(Custom);
				ModelVersion modelVersion = modelVersionService.selectByModelVersionStatus(mv);
				int myret = 1;
				// 查看商城配置信息
				ApkStoreConfiguration apkobj = null;
				// 没有匹配的机型版本
				if (modelVersion == null) {
					myret = -1;
				} else {
					Integer mvid = modelVersion.getId();
					apkobj = apkStoreConfigurationService.selectByStoreStatus(mvid);
				}
				// 查看商城配置信息,没有配置推送
				if (apkobj == null) {
					myret = -1;
				}
				// 没有对应的机型版本或者是没有商城推送配置，默认按照ip获取所在区域推送
				if (myret == -1) {
					// 创建ip对象工具
					IPAddressUtils ip = new IPAddressUtils();
					ip.init();
					System.out.println(Util.getIpByteArrayFromString(IpAddress));
					if (Util.getIpByteArrayFromString(IpAddress) != null) {
						// 通过ip调用工具类获取所在的国家地区和供应商
						System.out.println("IP地址[" + IpAddress + "]获取到的区域信息:" + ip.getIPLocation(IpAddress).getCountry()
								+ ", 获取到的城市:" + ip.getIPLocation(IpAddress).getCity() + ", 运营商:"
								+ ip.getIPLocation(IpAddress).getArea());
						// 通过ip获取对应的国家地区
						String country = ip.getIPLocation(IpAddress).getCountry();
						// 初始化查询条件
						AppUpgrade recordApp = new AppUpgrade();
						recordApp.setType(1);
						recordApp.setCustomer(country);
						AppUpgrade apkUpgrade = appUpgradeService.selectByNameApkList(recordApp);
						if (apkUpgrade != null) {
							String apkListString = apkUpgrade.getApkidlist();
							if (!(apkListString == null || ("").equals(apkListString))) {
								// 拆开APP升级列表对应的apkid字段
								String[] arr = apkListString.split(",");
								for (String strid : arr) {
									Integer apkid = Integer.valueOf(strid);
									System.out.println("拆开apkid查看：" + strid);
									for (Appstore appstore : listApp) {
										if (appstore.getId().equals(apkid)) {
											// 查询apk类型表
											AppType type = appTypeService.selectByPrimaryKey(appstore.getApktype());
											AppStoreUpgrade app = new AppStoreUpgrade();
											// apk类型编号
											app.setTypeId(type.getId());
											// 根据终端设备语言类型，获取对应的语言配置的pak类型名称
											String typeName = LanguageUtil.getLanguage(Language,
													String.valueOf(type.getId()));
											// apk类型名称
											app.setTypeName(typeName);
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
											storeUpgradelist.add(app);
											continue;
										}
									}
								}
								// 集合对象转换json对象
								Gson gson = new Gson();
								String str = gson.toJson(storeUpgradelist);
								System.out.println("商城APP通过ip所在国家获取集合转换成json字符串对象：" + str);
								json.put("code", 1);
								json.put("msg", "success");
								json.put("data", str);
								return json;
							}

						}
					}
					json.put("code", 1);
					json.put("msg", "No store configuration information found.");
					json.put("data", null);
					return json;
				}

				// apkid集合字段
				String apkIdList = "";
				// 状态为1，匹配通过
				if (apkobj.getStatus() == 1) {
					// 获取配置关联apk列表对象
					AppUpgrade storeAppUpgrade = appUpgradeService.selectByPrimaryKey(apkobj.getAppUpgradeId());
					// 不存在商城推送策略，默认通过Android版本去匹配
					if (storeAppUpgrade == null || storeAppUpgrade.getApkidlist() == null
							|| ("").equals(storeAppUpgrade.getApkidlist())) {
						// 获取默认安装的Android对象
						AppUpgrade defaultStoreApp = null;
						// 默认按Android版本查询
						List<AppUpgrade> defaultStoreList = appUpgradeService
								.selectByDefaultStoreApkList(AndroidVersion);
						if (defaultStoreList.size() == 0) {
							json.put("code", 1);
							json.put("msg", "Did not find the added mall app.");
							json.put("data", null);
							return json;
						} else {
							defaultStoreApp = defaultStoreList.get(0);
						}
						// 默认Android版本推送为NUll或没有添加APP
						if (defaultStoreApp == null || defaultStoreApp.getApkidlist() == null
								|| ("").equals(defaultStoreApp.getApkidlist())) {
							json.put("code", 1);
							json.put("msg", "Did not find the added mall app.");
							json.put("data", null);
							return json;
						} else {
							apkIdList = defaultStoreApp.getApkidlist();
						}
					}
					// 商城存在添加上传apk的策略的情况，
					else {
						apkIdList = storeAppUpgrade.getApkidlist();
					}
					// 拆开APP升级列表对应的apkid字段
					String[] arr = apkIdList.split(",");
					for (String strid : arr) {
						Integer apkid = Integer.valueOf(strid);
						System.out.println("拆开apkid查看：" + strid);
						for (Appstore appstore : listApp) {
							if (appstore.getId().equals(apkid)) {
								// 查询apk类型表
								AppType type = appTypeService.selectByPrimaryKey(appstore.getApktype());
								AppStoreUpgrade app = new AppStoreUpgrade();
								// apk类型编号
								app.setTypeId(type.getId());
								// 根据终端设备语言类型，获取对应的语言配置的pak类型名称
								String typeName = LanguageUtil.getLanguage(Language, String.valueOf(type.getId()));
								// apk类型名称
								app.setTypeName(typeName);
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
								storeUpgradelist.add(app);
								continue;
							}

						}
					}

				}
				// 集合对象转换json对象
				Gson gson = new Gson();
				String str = gson.toJson(storeUpgradelist);
				System.out.println("商城APP获取集合转换成json字符串对象：" + str);
				json.put("code", 1);
				json.put("msg", "success");
				json.put("data", str);
			} catch (Exception e) {
				json.put("code", -1);
				json.put("msg", "Mall obtains APP information abnormally");
				json.put("data", null);
				e.printStackTrace();
			}
		} else {
			json.put("code", -1);
			json.put("msg", "An exception occurred in the black and white list");
			json.put("data", null);
		}
		return json;

	}
	
	
	
	/***
	 * APP自身升级检测
	 * 
	 * @param jsonRec
	 * @return
	 * @throws UnknownHostException
	 */
	@RequestMapping(value = "/appUpgrade", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject appUpgradeRecord(HttpServletRequest jsonRec) throws UnknownHostException {
		JSONObject json = new JSONObject();
		// 终端以post方式传递json字符串，获取数据流
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(jsonRec.getInputStream(), "UTF-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line = null;
		StringBuilder sb = new StringBuilder();
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String objectStr = sb.toString();
		System.out.println("APP自动升级获取终端数据：" + objectStr);
		// Json字符串转Object
		Map<Object, Object> map = new Gson().fromJson(objectStr, Map.class);

		// CPUsn序列号
		String CPUSerialNo = map.get("CPUSerialNo").toString();
		String Language = map.get("Language").toString();
		String DeviceDpi = map.get("DeviceDpi").toString();
		String ProductName = map.get("ProductName").toString();

		String Factory = map.get("Factory").toString();
		String Country = map.get("Country").toString();
		// 客户名称
		String Custom = map.get("Custom").toString();
		String Brand = map.get("Brand").toString();

		// 获取终端访问的IP地址
		String IpAddress = getIpAddr(jsonRec);
		// 设备机型
		String Model = map.get("Model").toString();
		String Hardware = map.get("Hardware").toString();
		String Serials = map.get("Serials").toString();

		String TotalMemory = map.get("TotalMemory").toString();
		String Board = map.get("Board").toString();
		String Fingerpint = map.get("Fingerpint").toString();
		// Mac地址
		String MacAddress = map.get("MacAddress").toString();
		String Type = map.get("Type").toString();

		String Device = map.get("Device").toString();
		String BuildTime = map.get("BuildTime").toString();
		// 日期字符串转换成时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(BuildTime);
			System.out.println(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String DeviceVersionCode = map.get("DeviceVersionCode").toString();
		String ScreenDensity = map.get("ScreenDensity").toString();
		String CPUName = map.get("CPUName").toString();

		String FreeMemory = map.get("FreeMemory").toString();
		// Android版本号
		String AndroidVersion = map.get("AndroidVersion").toString();
		System.out.println("终端传来的Mac：  " + MacAddress);

		// 数据填充
		Box box = new Box();
		box.setMac(MacAddress);
		box.setSn(Serials);
		box.setLanguage(Language);
		box.setDevicedpi(DeviceDpi);
		box.setProductname(ProductName);
		box.setFactory(Factory);
		box.setCustomer(Custom);
		box.setBrand(Brand);
		box.setLastLoginIp(IpAddress);
		box.setModel(Model);
		box.setHardwareversion(Hardware);
		box.setCpuserialno(CPUSerialNo);
		box.setTotalmemory(TotalMemory);
		box.setBoard(Board);
		box.setFingerpint(Fingerpint);
		box.setBoxtype(Type);
		box.setDevice(Device);
		box.setBuildtime(date);
		box.setDeviceVersionCode(DeviceVersionCode);
		box.setScreendensity(ScreenDensity);
		box.setCpuName(CPUName);
		box.setFreememory(FreeMemory);
		box.setBuildversion(AndroidVersion);
		box.setAndroidVersion(AndroidVersion);
		box.setOnlinetime(new Date());
		box.setCountry(Country);
		box.setCreatetime(new Date());
		try {
			// 判断盒子是否访问过
			if (boxService.selectMacSn(MacAddress, Serials) != null) {
				boxService.updateByPrimaryKeySelective(box);
			} else {
				boxService.insertSelective(box);
			}
		} catch (Exception e) {
			json.put("code", -1);
			json.put("msg", "Device entry failed");
			json.put("data", null);
			e.printStackTrace();
		}

		// 初始化设备黑白名单比较信息
		Filtering record = new Filtering();
		record.setModel(Model);
		record.setSn(Serials);
		record.setMac(MacAddress);
		// 返回设备黑白名单状态
		int status = filteringService.getBWStatus(record);
		// 获取APP自动升级推送的APP集合
		List<Appstore> appUpgradelist = new ArrayList<Appstore>();
		// -1加入黑名单，拦截无法继续访问
		if (status == -1) {
			System.out.println("APP自动升级黑名单拦截Mac：" + MacAddress);
			json.put("code", -1);
			json.put("msg", "Device has been blacklisted.");
			json.put("data", null);
		}
		// 1加入白名单，继续访问
		else if (status == 1) {
			try {
				// 获取所有所有上传的APP集合
				List<Appstore> listApp = appStoreService.getByAppstoreAll();
				// 判断没有apk
				if (listApp.size() == 0) {
					json.put("code", 1);
					json.put("msg", "No mall APP push message.");
					json.put("data", null);
					return json;
				}
				//传递升级的对象
				Appstore apkRecord = new Appstore();
				// 初始化查询条件
				AppUpgrade recordApp = new AppUpgrade();
				recordApp.setType(-1);
				//随便存储的一个字符
				recordApp.setCustomer("vs");
				AppUpgrade apkUpgrade = appUpgradeService.selectByNameApkList(recordApp);
				//用于判断是否存在apk升级
				int myRet = 0;
				if (apkUpgrade != null) {
					String apkListString = apkUpgrade.getApkidlist();
					if (!(apkListString == null || ("").equals(apkListString))) {
						// 拆开APP升级列表对应的apkid字段
						String[] arr = apkListString.split(",");
						for (String strid : arr) {
							Integer apkid = Integer.valueOf(strid);
							System.out.println("拆开apkid查看：" + strid);
							for (Appstore appstore : listApp) {
								if (appstore.getId().equals(apkid)) {
									Appstore app = new Appstore();
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
									appUpgradelist.add(app);
									continue;
								}
							}
						}
						
						// 存在apk自动升级对象集合的情况，apk版本升级比较
						if (appUpgradelist.size() != 0) {
							// 获取终端apk的jsonList缓存信息
							Gson gson1 = new Gson();
							String jsonApp = gson1.toJson(map.get("jsonApp"));
							
							ApkInfo objApk = gson1.fromJson(jsonApp,ApkInfo.class);
							System.out.println("-------获取的终端pak缓存信息-------" + objApk);
							//按照版本号排序，
							Collections.sort(appUpgradelist);
							
							for (Appstore apk : appUpgradelist) {
								// 存在静默安装缓存文件，需要对比升级安装
								if (objApk != null) {
									// apk名称
									String apkName = objApk.getApkName();
									// apk包名
									String packageName = objApk.getPackageName();
									// apk版本号
									int versionCode = objApk.getVersionCode();
									// 比较apk名称相同，版本比已安装的高，赋值传递升级对象
									if (apk.getApkname().equals(apkName) && apk.getApkpackagename().equals(packageName)
											&& Integer.valueOf(apk.getApkversion()) > versionCode) {
										// pak下载次数累计
//										appStoreService.updateForDownloadTotal(apk.getId());
										//赋值获取的最新升级版本
										System.out.println("应用市场apk更新描述：\n"+apk.getApkinfo());
										apkRecord = apk;
										myRet=1;
									} 
								} 
							}
						}
					}
				} 
				//查询存在升级apk情况
				if(myRet==1) {
					// 对象转换json字符串
					Gson gson = new Gson();
					String str = gson.toJson(apkRecord);
					System.out.println(apkRecord+"《《====应用市场自动升级json字符串对象：" + str);
					json.put("code", 1);
					json.put("msg", "success");
					json.put("data", str);
					return json;
				}else {
					json.put("code", 1);
					json.put("msg", "No new upgraded version detected.");
					json.put("data", null);
				}
				
			} catch (Exception e) {
				json.put("code", -1);
				json.put("msg", "Mall obtains APP information abnormally");
				json.put("data", null);
				e.printStackTrace();
			}
		} else {
			json.put("code", -1);
			json.put("msg", "An exception occurred in the black and white list");
			json.put("data", null);
		}
		return json;

	}

	/****
	 * *OTA升级获取xml路径
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public String softUpdate(HttpServletRequest request) throws UnknownHostException {
		int id = Integer.valueOf(request.getParameter("id"));
		String pack = request.getParameter("pack");
		String kodiversion = request.getParameter("kodiversion");
		String kodiaddsversion = request.getParameter("kodiaddsversion");
		String updating_apk_version = request.getParameter("updating_apk_version");
		String brand = request.getParameter("brand");
		String device = request.getParameter("device");
		String board = request.getParameter("board");
		// 终端传的是sn序列号
		String sn = request.getParameter("mac");
		String time = request.getParameter("time");
		String android = request.getParameter("android");
		String builder = request.getParameter("builder");
		String fingerprint = request.getParameter("fingerprint");
		String firmware = request.getParameter("firmware");
		String ip = getIpAddr(request);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		System.out.println(sn + " " + firmware + " " + device + " " + id + " " + brand + " " + board + " " + android
				+ " " + updating_apk_version + " " + df.format(new Date()) + " " + ip);
		// 数据填充
		Box box = new Box();
		box.setSn(sn);
		box.setBrand(brand);
		box.setLastLoginIp(ip);
		box.setFingerpint(fingerprint);
		box.setBoard(board);
		box.setFirmwareVersion(firmware);
		box.setDevice(device);
		box.setBuildversion(builder);
		box.setAndroidVersion(android);
		box.setOnlinetime(new Date());
		box.setCreatetime(new Date());
		try {
			/**
			 * 根据sn拼接一个mac地址格式字符串，(仅仅适用于mac和sn相同情况)
			 */
			StringBuffer sb = new StringBuffer();//构造一个StringBuilder对象
			for (int i = 0; i < sn.length(); i++) {
				if(0 < i && i%2 == 0) {
					sb.append(":");	//在字符串偶数位，将":"拼接进去
				}
				sb.append(sn.charAt(i));
			}
			//获取拼接mac字符串
			String mac = sb.toString();
			//ota拼接打印mac
			System.out.println("ota拼接后打印mac："+mac);
			//初始化mac
			box.setMac(mac);
			// 判断盒子是否访问过,若有访问记录修改对应信息
			if (boxService.selectMacSn(mac, sn) != null) {
				boxService.updateByOTAPrimaryKeySelective(box);
			}else {
				boxService.insertSelective(box);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// OTA升级返回返回Mac过滤拦截状态码
		int status = xmlFilterService.getByXmlMacFilerStatus(sn);
		System.out.println("拦截状态==========" + status);
		// -1加入黑名单，拦截无法继续访问
		if (status == -1) {
			System.out.println("OTA拦截序列号：" + sn);
			return "index";
		}
		// 1加入白名单，继续访问
		else if (status == 1) {
			// 初始化查询的xml文件匹配信息
			XmlFile xmlFile = new XmlFile();
			xmlFile.setAndroid(android);
			xmlFile.setDevice(device);
			xmlFile.setFirmware(firmware);
			// 获取匹配得到的xml文件信息
			XmlFile file = xmlFileService.selectByXmlFile(xmlFile);
			System.out.println("xml文件信息对象==========" + file);
			if (file != null) {
				String url = file.getXmlurl();
				System.out.println(device + android + "\tURL：" + url);
				url = "url=" + url;
				return url;
			}
		}
		return "index";
	}

	// 访问的IP过滤
	public String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

}
