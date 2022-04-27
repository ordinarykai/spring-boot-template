package com.kai.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author kai
 * @date 2022/3/12 20:33
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Role对象", description = "角色表")
@TableName("t_role")
public class Role extends Model<Role> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色id")
    @TableId(value = "role_id", type = IdType.AUTO)
    @NotEmpty(message = "角色id不能为空")
    @Size(max = 16, message = "角色id长度过长")
    private Integer roleId;

    @ApiModelProperty(value = "角色名称")
    @NotEmpty(message = "角色名称不能为空")
    @Size(max = 20, message = "角色名称长度过长")
    private String roleName;

    @ApiModelProperty(value = "角色描述")
    @NotEmpty(message = "角色描述不能为空")
    @Size(max = 50, message = "角色描述长度过长")
    private String description;

    @ApiModelProperty(value = "角色状态（1.正常 0.禁用）")
    @NotEmpty(message = "角色状态（1.正常 0.禁用）不能为空")
    @Size(max = 1, message = "角色状态（1.正常 0.禁用）长度过长")
    private Integer status;

    @ApiModelProperty(value = "创建人 外联t_admin(admin_id)")
    @NotEmpty(message = "创建人 外联t_admin(admin_id)不能为空")
    @Size(max = 16, message = "创建人 外联t_admin(admin_id)长度过长")
    private String createBy;

    @ApiModelProperty(value = "权限id集合")
    @NotEmpty(message = "权限id集合不能为空")
    private String permissionIds;

    @ApiModelProperty(value = "乐观锁")
    @NotNull(message = "乐观锁不能为空")
    private Integer version;

    @ApiModelProperty(value = "逻辑删除（Y.已删除 N.未删除）")
    @NotEmpty(message = "逻辑删除（Y.已删除 N.未删除）不能为空")
    @Size(max = 1, message = "逻辑删除（Y.已删除 N.未删除）长度过长")
    private String deleted;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "创建时间不能为空")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "修改时间不能为空")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.roleId;
    }

}