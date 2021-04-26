package com.yanfeitech.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.yanfeitech.application.common.Global;

/**
 * 
 * <p>
 * Title: WebMvcConfig
 * </p>
 * <p>
 * Description:静态资源映射配置
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("file:" + Global.getBaseDir());
	}
}