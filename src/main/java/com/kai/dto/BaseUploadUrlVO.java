package com.kai.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author kai
 * @date 2022/3/12 14:41
 */
@Data
public class BaseUploadUrlVO {

    @ApiModelProperty(value = "文件读取地址前缀")
    private String baseUploadUrl;

}
