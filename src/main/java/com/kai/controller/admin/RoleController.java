package com.kai.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kai.config.api.Result;
import com.kai.bo.dto.RoleAddDTO;
import com.kai.bo.dto.RolePageDTO;
import com.kai.bo.dto.RoleUpdateDTO;
import com.kai.bo.vo.RoleVO;
import com.kai.service.RoleService;
import com.kai.util.bo.SelectVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author kai
 * @date 2022/3/12 20:10
 */
@Api(tags = "角色管理")
@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("querySelect")
    @ApiOperation(value = "查询角色下拉框", notes = "查询角色下拉框")
    public Result<List<SelectVO>> querySelect() {
        List<SelectVO> list = roleService.querySelect();
        return Result.success(list);
    }

    @PostMapping("add")
    @ApiOperation(value = "添加角色", notes = "添加角色")
    public Result<Void> add(@RequestBody @Valid RoleAddDTO req) {
        boolean b = roleService.add(req);
        return b ? Result.success() : Result.failed();
    }

    @PostMapping("update")
    @ApiOperation(value = "编辑角色", notes = "编辑角色")
    public Result<Void> update(@RequestBody @Valid RoleUpdateDTO dto) {
        boolean b = roleService.update(dto);
        return b ? Result.success(null, "保存成功") : Result.failed("保存失败");
    }

    @DeleteMapping("delete")
    @ApiOperation(value = "删除角色", notes = "删除角色")
    public Result<Void> delete(
            @RequestParam(value = "roleIds") List<Integer> roleIds
    ) {
        boolean b = roleService.delete(roleIds);
        return b ? Result.success(null, "删除成功") : Result.failed("删除失败");
    }

    @GetMapping("pageQuery")
    @ApiOperation(value = "查询角色分页列表", notes = "查询角色分页列表")
    public Result<IPage<RoleVO>> pageQuery(@Valid RolePageDTO req) {
        IPage<RoleVO> page = roleService.pageQuery(req);
        return Result.success(page);
    }

    @GetMapping("detail")
    @ApiOperation(value = "查询角色详情", notes = "查询角色详情")
    public Result<RoleVO> query(
            @RequestParam(value = "roleId") Integer roleId
    ) {
        RoleVO res = roleService.query(roleId);
        return Result.success(res);
    }

    @PostMapping("updateStatus")
    @ApiOperation(value = "启用/禁用角色", notes = "启用/禁用角色")
    public Result<Void> updateStatus(
            @RequestParam(value = "roleId") Integer roleId
    ) {
        return roleService.updateStatus(roleId);
    }

}
