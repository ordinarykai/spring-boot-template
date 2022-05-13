package com.kai.bo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author kai
 * @date 2022/3/12 20:22
 */
@Data
public class RoleAddDTO {

    @ApiModelProperty(value = "角色名称",example = "管理员")
    @NotBlank(message = "请输入角色名称")
    @Size(min = 2, max = 20, message = "角色名称限定2-20位字符")
    private String roleName;

    @ApiModelProperty(value = "描述",example = "管理员")
    @Size(max = 50, message = "描述最长不超过50字符")
    private String description;

    @ApiModelProperty(value = "功能项id集合, 用英文,分隔",example = "1,2,3")
    private String funIds;

}