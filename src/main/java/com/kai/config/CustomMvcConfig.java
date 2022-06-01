package com.kai.config;

import com.easy.boot.core.constant.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author kai
 * @date 2022/3/12 14:56
 */
@Configuration
public class CustomMvcConfig implements WebMvcConfigurer, WebMvcRegistrations {

    /**
     * springboot配置文件当前环境
     */
    @Value("${spring.profiles.active}")
    private String env;

    /**
     * 配置静态资源的访问
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!env.equals(Constants.PRO_ENV)) {
            // 内部权限编辑页面放在resources下的permission目录，生产环境严禁开启访问
            registry.addResourceHandler("/permission/**")
                    .addResourceLocations("classpath:/permission/");
        }
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

}
