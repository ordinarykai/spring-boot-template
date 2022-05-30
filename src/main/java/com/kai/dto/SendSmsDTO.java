package com.kai.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author kai
 * @date 2022/3/12 14:40
 */
@Data
public class SendSmsDTO {

    @ApiModelProperty(value = "手机号", required = true)
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "[0-9]{11}", message = "手机号不合法")
    private String phone;

}
