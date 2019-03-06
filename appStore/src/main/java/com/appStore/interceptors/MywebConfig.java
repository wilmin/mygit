package com.appStore.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MywebConfig implements WebMvcConfigurer {
	@Autowired
	private LoginInterceptor loginInterceptor;

	// 这个方法是用来配置静态资源的，比如html，js，css，等等
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 配置模板资源路径
		registry.addResourceHandler("/templates/**")
				.addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/templates/");
		registry.addResourceHandler("/static/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/static/");
	}

	// 这个方法用来注册拦截器，我们自己写好的拦截器需要通过这里添加注册才能生效
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// addPathPatterns("/**") 表示拦截所有的请求，
		// excludePathPatterns("/login") 表示除了登陆，静态文件之外，其他的都拦截
		/**
		 * /login.html登陆界面，/checkin登陆的control接口
		 * /static/**静态文件加载，/upload/**文件下载路径
		 * /autoConfiguration自动配置接口，/staticConfiguration静默安装接口
		 * /storeAppList商城推送接口，/update OTA升级接口
		 */
		registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns("/login.html", "/checkin",
				"/static/**", "/upload/**", "/autoConfiguration", "/staticConfiguration", "/storeAppList", "/update","/appUpgrade");
	}
}
