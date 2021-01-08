package com.jq.kafkaui.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 添加日志拦截
 * @author jiangqiang
 * @date 2019年4月14日上午12:10:36
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	private LogInterceptor logInterceptor;

	/**
	 * 添加拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(logInterceptor).addPathPatterns("/**").excludePathPatterns("/health");
	}


}