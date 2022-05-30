package com.kai.dto;

import com.kai.entity.Permission;
import com.kai.service.PermissionService;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import static com.kai.Application.applicationContext;

/**
 * @author kai
 */
@Data
public class PermissionVO {

    @ApiModelProperty(value = "权限id")
    private Integer permissionId;

    @ApiModelProperty(value = "父级权限id (parentId=0表示顶级权限 parentId=-1表示自动生成还没有关联的权限)")
    private Integer parentId;

    @ApiModelProperty(value = "类型 (1.菜单 2.按钮)")
    private Integer type;

    @ApiModelProperty(value = "权限名称")
    private String name;

    @ApiModelProperty(value = "uri (菜单权限是页面的uri，按钮权限是method:uri)")
    private String uri;

    @ApiModelProperty(value = "菜单图标 (菜单权限有效)")
    private String icon;

    @ApiModelProperty(value = "排序号")
    private Integer num;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "子菜单")
    private List<PermissionVO> children;

    @ApiModelProperty(value = "父级权限名称")
    public String getParentName() {
        if(parentId == null){
            return "";
        }
        Permission permission = applicationContext.getBean(PermissionService.class).getById(parentId);
        return permission == null ? "" : permission.getName();
    }

}
