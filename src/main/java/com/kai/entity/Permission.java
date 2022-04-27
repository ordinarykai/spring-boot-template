package com.kai.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.kai.service.PermissionService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

import static com.kai.Application.applicationContext;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author kai
 * @since 2022-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_permission")
@ApiModel(value="Permission对象", description="权限表")
public class Permission extends Model<Permission> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "权限id")
    @TableId(value = "permission_id", type = IdType.AUTO)
    private Integer permissionId;

    @ApiModelProperty(value = "父级权限id (parentId=0表示顶级权限 parentId=-1表示自动生成还没有关联的权限)")
    @NotNull(message = "父级权限id不能为空")
    private Integer parentId;

    @ApiModelProperty(value = "类型 (1.菜单 2.按钮)")
    @NotNull(message = "类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "权限等级")
    @NotNull(message = "权限等级不能为空")
    private Integer level;

    @ApiModelProperty(value = "权限名称")
    @NotBlank(message = "权限名称不能为空")
    private String name;

    @ApiModelProperty(value = "uri (菜单权限是页面的uri，按钮权限是method:uri)")
    @NotBlank(message = "uri不能为空")
    private String uri;

    @ApiModelProperty(value = "菜单图标 (菜单权限有效)")
    private String icon;

    @ApiModelProperty(value = "排序号")
    @NotNull(message = "排序号不能为空")
    private Integer num;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "父级权限名称")
    public String getParentName() {
        if(parentId == null){
            return "";
        }
        Permission permission = applicationContext.getBean(PermissionService.class).getById(parentId);
        return permission == null ? "" : permission.getName();
    }

    @Override
    protected Serializable pkVal() {
        return this.permissionId;
    }

}
