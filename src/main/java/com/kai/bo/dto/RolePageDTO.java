package com.kai.bo.dto;

import com.kai.boot.util.bo.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author kai
 * @date 2022/3/12 20:22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RolePageDTO extends PageDTO {

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "状态（Y.正常 N.禁用）")
    private String status;

}
