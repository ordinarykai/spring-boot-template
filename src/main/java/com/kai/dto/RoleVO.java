package com.kai.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author kai
 * @date 2022/3/12 20:23
 */
@Data
public class RoleVO {

    @ApiModelProperty("角色id")
    private Integer roleId;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "功能id集合, 用英文,分隔")
    private String funIds;

    @ApiModelProperty(value = "角色状态 (Y.正常 N.禁用)")
    private String status;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "创建人")
    private String createdBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

}
