package com.appStore;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling				//定时任务注解
@MapperScan("com.appStore.dao")
@SpringBootApplication
public class AppStoreApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(AppStoreApplication.class, args);
	}

	/**
	 ** 需要把web项目打成war包部署到外部tomcat运行时需要改变启动方式    
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(AppStoreApplication.class);
	}

}
