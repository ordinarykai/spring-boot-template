package com.kai.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author kai
 */
@Data
public class CaptchaVO {
    
    @ApiModelProperty(value = "验证码uuid", example = "123456")
    private String uuid;
    
    @ApiModelProperty(value = "验证码base64格式数据")
    private String base64;
    
}
