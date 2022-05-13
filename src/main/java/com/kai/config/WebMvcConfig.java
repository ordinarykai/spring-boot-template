package com.kai.config;

import com.kai.config.auth.AuthInterceptor;
import com.kai.config.auth.AuthUriConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

import static com.kai.util.constant.CommonConstant.PRO_ENV;

/**
 * @author kai
 * @date 2022/3/12 14:56
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private AuthUriConfig authUriConfig;
    @Resource
    private AuthInterceptor authInterceptor;
    @Resource
    private UploadConfig uploadConfig;
    /**
     * springboot配置文件当前环境
     */
    @Value("${spring.profiles.active}")
    private String env;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册用户认证授权拦截器
        registry.addInterceptor(authInterceptor)
                .addPathPatterns(authUriConfig.getAddPathPatterns())
                .excludePathPatterns(authUriConfig.getExcludePathPatterns());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 解决跨域问题
        // springboot版本是2.5.8，设置allowedOrigins("*")且.allowCredentials(true)会报错
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("POST", "GET", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600);
    }

    /**
     * 配置静态资源的访问
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 内部权限编辑页面放在resources下的permission目录，生产环境严禁开启访问
        if (!env.equals(PRO_ENV)) {
            registry.addResourceHandler("/permission/**")
                    .addResourceLocations("classpath:/permission/");
        }
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:" + uploadConfig.getPath());
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

}
