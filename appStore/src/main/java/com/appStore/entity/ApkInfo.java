package com.appStore.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApkInfo {
	public static final String APPLICATION_ICON_120 = "application-icon-120";
	public static final String APPLICATION_ICON_160 = "application-icon-160";
	public static final String APPLICATION_ICON_240 = "application-icon-240";
	public static final String APPLICATION_ICON_320 = "application-icon-320";
	private Integer apkid;
	private String apkName;
	private String packageName; 
	private int versionCode; 
	private String versionName; 
	private String url; 
	private String size;
	 /**
	 * 支持的android平台最低版本号
	 */
	private String minSdkVersion = null;
	 /**
	  * apk所需要的权限
	  */
	private List<String> usesPermissions = null;
	 /**
	 * 支持的SDK版本。
	 */
	private String sdkVersion;
	 /**
	 * 建议的SDK版本
	 */
	private String targetSdkVersion;
	 /**
	  * 各个分辨率下的图标的路径。
	  */
	private Map<String, String> applicationIcons;
	 /**
	 * 程序的图标。
	 */
	private String applicationIcon;
	/**
	 * 暗指的特性。
	 */
	private List<ImpliedFeature> impliedFeatures;
	 /**
	 * 所需设备特性。
	 */
	private List<String> features;
	 /**
	 * 启动界面
	 */
	private String launchableActivity;

	public ApkInfo() {
		this.usesPermissions = new ArrayList<String>();
		this.applicationIcons = new HashMap<String, String>();
		this.impliedFeatures = new ArrayList<ImpliedFeature>();
		this.features = new ArrayList<String>();
	}
	
	public String getMinSdkVersion() {
		return minSdkVersion;
	}

	public void setMinSdkVersion(String minSdkVersion) {
		this.minSdkVersion = minSdkVersion;
	}

	public List<String> getUsesPermissions() {
		return usesPermissions;
	}

	public void setUsesPermissions(List<String> usesPermissions) {
		this.usesPermissions = usesPermissions;
	}

	public String getSdkVersion() {
		return sdkVersion;
	}

	public void setSdkVersion(String sdkVersion) {
		this.sdkVersion = sdkVersion;
	}

	public String getTargetSdkVersion() {
		return targetSdkVersion;
	}

	public void setTargetSdkVersion(String targetSdkVersion) {
		this.targetSdkVersion = targetSdkVersion;
	}

	public Map<String, String> getApplicationIcons() {
		return applicationIcons;
	}

	public void setApplicationIcons(Map<String, String> applicationIcons) {
		this.applicationIcons = applicationIcons;
	}

	public String getApplicationIcon() {
		return applicationIcon;
	}

	public void setApplicationIcon(String applicationIcon) {
		this.applicationIcon = applicationIcon;
	}

	public List<ImpliedFeature> getImpliedFeatures() {
		return impliedFeatures;
	}

	public void setImpliedFeatures(List<ImpliedFeature> impliedFeatures) {
		this.impliedFeatures = impliedFeatures;
	}

	public List<String> getFeatures() {
		return features;
	}

	public void setFeatures(List<String> features) {
		this.features = features;
	}

	public String getLaunchableActivity() {
		return launchableActivity;
	}

	public void setLaunchableActivity(String launchableActivity) {
		this.launchableActivity = launchableActivity;
	}

	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	public Integer getApkid() {
		return apkid;
	}
	public void setApkid(Integer apkid) {
		this.apkid = apkid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getApkName() {
		return apkName;
	}
	public void setApkName(String apkName) {
		this.apkName = apkName;
	}
	public void addToApplicationIcons(String key, String value) {
	      this.applicationIcons.put(key, value);
	}

	public void addToImpliedFeatures(ImpliedFeature impliedFeature) {
	      this.impliedFeatures.add(impliedFeature);
	}
	public void addToUsesPermissions(String usesPermission) {
	      this.usesPermissions.add(usesPermission);
	}
	public void addToFeatures(String feature) {
	      this.features.add(feature);
	}
	@Override
	public String toString() {
	      return "ApkInfo [versionCode=" + versionCode + ",\n versionName="
	 + versionName + ",\n packageName=" + packageName
	 + ",\n minSdkVersion=" + minSdkVersion + ",\n usesPermissions="
	 + usesPermissions + ",\n sdkVersion=" + sdkVersion
	 + ",\n targetSdkVersion=" + targetSdkVersion
	 + ",\n applicationLable=" + apkName
	 + ",\n applicationIcons=" + applicationIcons
	 + ",\n applicationIcon=" + applicationIcon
	 + ",\n impliedFeatures=" + impliedFeatures + ",\n features="
	 + features + ",\n launchableActivity=" + launchableActivity + "\n]";
	}

}
