package com.appStore.tools;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageUtil {

	public static String getLanguage(String lang, String typeid) {
		// 设置语言环境
		/*
		 * Locale cn = Locale.CHINA;//中文 Locale us = Locale.US;//英文
		 */

		// 默认加载英语
		// 根据基名和语言环境加载对应的语言资源文件
		ResourceBundle myResources = ResourceBundle.getBundle("myproperties_en", Locale.getDefault());

		switch (lang) {
		case "en":
			// 英语 加载myproperties_en.properties
			myResources = ResourceBundle.getBundle("myproperties_en", Locale.getDefault());
			break;
		case "eg":
			// 阿拉伯文 (埃及)
			myResources = ResourceBundle.getBundle("myproperties_eg", Locale.getDefault());
			break;
		case "de":
			// 德文 (德国)
			myResources = ResourceBundle.getBundle("myproperties_de", Locale.getDefault());
			break;
		case "fr":
			// 法文 (法国)
			myResources = ResourceBundle.getBundle("myproperties_fr", Locale.getDefault());
			break;
		case "il":
			// 希伯来文 (以色列)
			myResources = ResourceBundle.getBundle("myproperties_il", Locale.getDefault());
			break;
		case "kr":
			// 朝鲜文 (南朝鲜)
			myResources = ResourceBundle.getBundle("myproperties_kr", Locale.getDefault());
			break;
		case "pl":
			// 波兰文 (波兰)
			myResources = ResourceBundle.getBundle("myproperties_pl", Locale.getDefault());
			break;
		case "br":
			// 葡萄牙文 (巴西)
			myResources = ResourceBundle.getBundle("myproperties_br", Locale.getDefault());
			break;
		case "ru":
			// 俄文 (俄罗斯)
			myResources = ResourceBundle.getBundle("myproperties_ru", Locale.getDefault());
			break;
		case "tr":
			// 土耳其文 (土耳其)
			myResources = ResourceBundle.getBundle("myproperties_tr", Locale.getDefault());
			break;
		case "ua":
			// 乌克兰文 (乌克兰)
			myResources = ResourceBundle.getBundle("myproperties_ua", Locale.getDefault());
			break;
		case "zh_cn":
			// 中午简体
			myResources = ResourceBundle.getBundle("myproperties_zh_cn", Locale.getDefault());
			break;
		case "zh_tw":
			// 中文繁体
			myResources = ResourceBundle.getBundle("myproperties_zh_tw", Locale.getDefault());
			break;
		default:
			// 其他没有配置的语言默认加载英文
			myResources = ResourceBundle.getBundle("myproperties_en", Locale.getDefault());
			break;
		}
		// 加载资源文件后， 程序就可以调用ResourceBundle实例对象的 getString方法获取指定的资源信息名称所对应的值。
		// String value = myResources.getString(“key");
		String username = myResources.getString(typeid);
		System.out.println("从配置文件获取的语言-------------" + username);
		return username;
	}

}
