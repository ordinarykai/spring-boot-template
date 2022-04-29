package com.kai.bo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author kai
 * @date 2022/3/12 20:27
 */
@Data
public class PermissionTreeVO {

    @ApiModelProperty(value = "权限id")
    private Integer permissionId;

    @ApiModelProperty(value = "父级权限id")
    private Integer parentId;

    @ApiModelProperty(value = "类型 (1.菜单 2.按钮)")
    private Integer type;

    @ApiModelProperty(value = "权限名称")
    private String name;

    @ApiModelProperty(value = "uri (菜单权限是页面的uri，按钮权限是method:uri)")
    private String uri;

    @ApiModelProperty(value = "菜单图标 (菜单权限有效)")
    private String icon;

    @ApiModelProperty("是否选中 (0.未选中 1.选中)")
    private Integer checkArr = 0;

    @ApiModelProperty("权限")
    private List<PermissionTreeVO> children;

}
