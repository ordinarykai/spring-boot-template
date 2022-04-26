package com.kai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件上传配置
 * @author kai
 * @date 2022/3/12 14:34
 */
@Data
@Component
@ConfigurationProperties(prefix = "upload")
public class UploadConfig {

    /**
     * 上传文件存储目录
     */
    private String path;

    /**
     * 文件读取地址
     */
    private String url;

}
