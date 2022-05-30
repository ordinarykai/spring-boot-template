package com.kai.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kai.dto.RoleAddDTO;
import com.kai.dto.RolePageDTO;
import com.kai.dto.RoleUpdateDTO;
import com.kai.dto.RoleVO;
import com.easy.boot.core.api.Result;
import com.easy.boot.core.util.bo.SelectVO;
import com.kai.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author kai
 * @date 2022/3/12 20:10
 */
@Api(tags = "角色管理")
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @GetMapping
    @ApiOperation(value = "查询角色分页列表", notes = "查询角色分页列表")
    public Result<IPage<RoleVO>> page(@Valid RolePageDTO req) {
        IPage<RoleVO> page = roleService.page(req);
        return Result.success(page);
    }

    @GetMapping("/select")
    @ApiOperation(value = "查询角色下拉框", notes = "查询角色下拉框")
    public Result<List<SelectVO>> select() {
        List<SelectVO> list = roleService.select();
        return Result.success(list);
    }

    @GetMapping("/{roleId}")
    @ApiOperation(value = "查询角色详情", notes = "查询角色详情")
    public Result<RoleVO> query(
            @PathVariable(value = "roleId") Integer roleId
    ) {
        RoleVO res = roleService.query(roleId);
        return Result.success(res);
    }

    @PostMapping
    @ApiOperation(value = "添加角色", notes = "添加角色")
    public Result<Void> add(@RequestBody @Valid RoleAddDTO req) {
        boolean b = roleService.add(req);
        return b ? Result.success() : Result.failed();
    }

    @PutMapping
    @ApiOperation(value = "编辑角色", notes = "编辑角色")
    public Result<Void> update(@RequestBody @Valid RoleUpdateDTO dto) {
        boolean b = roleService.update(dto);
        return b ? Result.success(null, "保存成功") : Result.failed("保存失败");
    }

    @PutMapping("/status/{roleId}")
    @ApiOperation(value = "启用/禁用角色", notes = "启用/禁用角色")
    public Result<Void> updateStatus(
            @PathVariable(value = "roleId") Integer roleId
    ) {
        return roleService.updateStatus(roleId);
    }

    @DeleteMapping("/{roleIds}")
    @ApiOperation(value = "删除角色", notes = "删除角色")
    public Result<Void> delete(
            @PathVariable(value = "roleIds") List<Integer> roleIds
    ) {
        boolean b = roleService.delete(roleIds);
        return b ? Result.success(null, "删除成功") : Result.failed("删除失败");
    }

}
