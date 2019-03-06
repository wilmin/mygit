package com.appStore.tools;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.util.ClassUtils;

import com.appStore.entity.ApkInfo;
import com.appStore.entity.ImpliedFeature;

/**
 * apk工具类。封装了获取Apk信息的方法。
 * 包名/版本号/版本名/应用程序名/支持的android最低版本号/支持的SDK版本/建议的SDK版本/所需设备特性等
 */
public class ApkUtil {
	public static final String VERSION_CODE = "versionCode";
	public static final String VERSION_NAME = "versionName";
	public static final String SDK_VERSION = "sdkVersion";
	public static final String TARGET_SDK_VERSION = "targetSdkVersion";
	public static final String USES_PERMISSION = "uses-permission";
	public static final String APPLICATION_LABEL = "application-label";
	public static final String APPLICATION_ICON = "application-icon";
	public static final String USES_FEATURE = "uses-feature";
	public static final String USES_IMPLIED_FEATURE = "uses-implied-feature";
	public static final String SUPPORTS_SCREENS = "supports-screens";
	public static final String SUPPORTS_ANY_DENSITY = "supports-any-density";
	public static final String DENSITIES = "densities";
	public static final String PACKAGE = "package";
	public static final String APPLICATION = "application:";
	public static final String LAUNCHABLE_ACTIVITY = "launchable-activity";

	private ProcessBuilder mBuilder;
	private static final String SPLIT_REGEX = "(: )|(=')|(' )|'";
	private static final String FEATURE_SPLIT_REGEX = "(:')|(',')|'";
	/**
	 * aapt所在的目录。
	 */
	// windows环境下直接指向appt.exe
	// 比如你可以放在src下 本地测试路径：E:\\aapt\\
//	private String mAaptPath = "D:\\aapt\\aapt.exe";
	// linux下
	private String mAaptPath = "/usr/local/apktool/aapt";

	public ApkUtil() {
		mBuilder = new ProcessBuilder();
		mBuilder.redirectErrorStream(true);
	}

	/**
	 * 返回一个apk程序的信息。
	 *
	 * @param apkPath apk的路径。
	 * @return apkInfo 一个Apk的信息。
	 */
	public ApkInfo getApkInfo(String apkPath) throws Exception {
		System.out.println("================================开始执行命令=========================");
		// 通过命令调用aapt工具解析apk文件
		Process process = mBuilder.command(mAaptPath, "d", "badging", apkPath).start();
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is, "utf-8");
		BufferedReader br = new BufferedReader(isr);
		String tmp = br.readLine();
		try {
			if (tmp == null || !tmp.startsWith("package")) {
				throw new Exception("参数不正确，无法正常解析APK包。输出结果为:\n" + tmp + "...");
			}
			ApkInfo apkInfo = new ApkInfo();
			do {
				setApkInfoProperty(apkInfo, tmp);
			} while ((tmp = br.readLine()) != null);
			return apkInfo;
		} catch (Exception e) {
			throw e;
		} finally {
			process.destroy();
			mBuilder.directory();
			
			if (br != null) {
				closeIO(br);
			}
			if (isr != null) {
				closeIO(isr);
			}
			if (is != null) {
				closeIO(is);
			}
		}
	}

	/**
	 * 设置APK的属性信息。
	 *
	 * @param apkInfo
	 * @param source
	 */
	private void setApkInfoProperty(ApkInfo apkInfo, String source) {
		if (source.startsWith(PACKAGE)) {
			splitPackageInfo(apkInfo, source);
		} else if (source.startsWith(LAUNCHABLE_ACTIVITY)) {
			apkInfo.setLaunchableActivity(getPropertyInQuote(source));
		} else if (source.startsWith(SDK_VERSION)) {
			apkInfo.setSdkVersion(getPropertyInQuote(source));
		} else if (source.startsWith(TARGET_SDK_VERSION)) {
			apkInfo.setTargetSdkVersion(getPropertyInQuote(source));
		} else if (source.startsWith(USES_PERMISSION)) {
			apkInfo.addToUsesPermissions(getPropertyInQuote(source));
		} else if (source.startsWith(APPLICATION_LABEL)) {
			// windows下获取应用名称
			apkInfo.setApkName(getPropertyInQuote(source));
		} else if (source.startsWith(APPLICATION_ICON)) {
			apkInfo.addToApplicationIcons(getKeyBeforeColon(source), getPropertyInQuote(source));
		} else if (source.startsWith(APPLICATION)) {
			String[] rs = source.split("( icon=')|'");
			apkInfo.setApplicationIcon(rs[rs.length - 1]);
			// linux下获取应用名称
			apkInfo.setApkName(rs[1]);
		} else if (source.startsWith(USES_FEATURE)) {
			apkInfo.addToFeatures(getPropertyInQuote(source));
		} else if (source.startsWith(USES_IMPLIED_FEATURE)) {
			apkInfo.addToImpliedFeatures(getFeature(source));
		} else {
//       System.out.println(source);
		}
	}

	private ImpliedFeature getFeature(String source) {
		String[] result = source.split(FEATURE_SPLIT_REGEX);
		ImpliedFeature impliedFeature = new ImpliedFeature(result[1], result[2]);
		return impliedFeature;
	}

	/**
	 * 返回出格式为name: 'value'中的value内容。
	 *
	 * @param source
	 * @return
	 */
	private String getPropertyInQuote(String source) {
		int index = source.indexOf("'") + 1;
		return source.substring(index, source.indexOf('\'', index));
	}

	/**
	 * 返回冒号前的属性名称
	 *
	 * @param source
	 * @return
	 */
	private String getKeyBeforeColon(String source) {
		return source.substring(0, source.indexOf(':'));
	}

	/**
	 * 分离出包名、版本等信息。
	 *
	 * @param apkInfo
	 * @param packageSource
	 */
	private void splitPackageInfo(ApkInfo apkInfo, String packageSource) {
		String[] packageInfo = packageSource.split(SPLIT_REGEX);
		apkInfo.setPackageName(packageInfo[2]);
		apkInfo.setVersionCode(Integer.valueOf(packageInfo[4]));
		apkInfo.setVersionName(packageInfo[6]);
	}

	/**
	 * 释放资源。
	 *
	 * @param c 将关闭的资源
	 */
	private final void closeIO(Closeable c) {
		if (c != null) {
			try {
				c.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * public static void main(String[] args) { try { String demo =
	 * "D:\\Java\\idea_app\\ReadApkAndIpa1\\src\\main\\java\\150211100441.apk";
	 * ApkInfo apkInfo = new ApkUtil().getApkInfo(demo);
	 * System.out.println(apkInfo); } catch (Exception e) { e.printStackTrace(); } }
	 */

	public String getmAaptPath() {
		return mAaptPath;
	}

	public void setmAaptPath(String mAaptPath) {
		this.mAaptPath = mAaptPath;
	}
}
