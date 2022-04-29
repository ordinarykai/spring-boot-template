package com.kai.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kai.bo.vo.PermissionVO;
import com.kai.config.api.Result;
import com.kai.config.redis.service.RedisService;
import com.kai.entity.Permission;
import com.kai.service.PermissionService;
import com.kai.util.bo.TreeSelectVO;
import com.kai.util.bo.TreeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static com.kai.util.constant.CommonConstant.DISABLE;
import static com.kai.util.constant.CommonConstant.ENABLE;
import static com.kai.util.constant.RedisConstant.REDIS_PERMISSION_TREE;

/**
 * @author kai
 * @date 2022/3/12 22:50
 */
@Api(tags = "权限管理")
@RestController
@RequestMapping("/api/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RedisService redisService;

    @GetMapping
    @ApiOperation(value = "查询权限分页列表", notes = "查询权限分页列表")
    public Result<IPage<PermissionVO>> page(
            Integer page, Integer size,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "parentName", required = false) String parentName,
            @RequestParam(value = "level", required = false) Integer level,
            @ApiParam("是否关联 (1.是 0.否)") @RequestParam(value = "associateStatus", required = false) Integer associateStatus
    ) {
        List<Integer> parentIds = new ArrayList<>();
        if (StringUtils.isNotBlank(parentName)) {
            LambdaQueryWrapper<Permission> parentQueryWrapper = Wrappers.lambdaQuery(Permission.class)
                    .like(StringUtils.isNotBlank(parentName), Permission::getName, parentName);
            parentIds = permissionService.list(parentQueryWrapper).stream().map(Permission::getPermissionId).collect(Collectors.toList());
        }
        if (DISABLE.equals(associateStatus)) {
            parentIds.add(-1);
        }
        LambdaQueryWrapper<Permission> queryWrapper = Wrappers.lambdaQuery(Permission.class)
                .like(StringUtils.isNotBlank(name), Permission::getName, name)
                .in(CollectionUtils.isNotEmpty(parentIds), Permission::getParentId, parentIds)
                .ne(ENABLE.equals(associateStatus), Permission::getParentId, -1)
                .eq(Objects.nonNull(level), Permission::getLevel, level)
                .orderByAsc(Permission::getLevel)
                .orderByAsc(Permission::getParentId)
                .orderByAsc(Permission::getNum);
        IPage<PermissionVO> permissionPage = permissionService.page(new Page<>(page, size), queryWrapper).convert(permission -> {
            PermissionVO permissionVO = new PermissionVO();
            BeanUtils.copyProperties(permission, permissionVO);
            permissionVO.setChildren(getChildren(permission.getPermissionId()));
            return permissionVO;
        });
        return Result.success(permissionPage);
    }

    public List<PermissionVO> getChildren(Integer parentId) {
        return permissionService.list(Wrappers.lambdaQuery(Permission.class).eq(Permission::getParentId, parentId))
                .stream().map(permission -> {
                    PermissionVO permissionVO = new PermissionVO();
                    BeanUtils.copyProperties(permission, permissionVO);
                    permissionVO.setChildren(getChildren(permission.getPermissionId()));
                    return permissionVO;
                }).collect(Collectors.toList());
    }

    @GetMapping("/tree/select")
    @ApiOperation(value = "查询权限下拉树", notes = "查询权限下拉树")
    public Result<List<TreeSelectVO>> treeSelect() {
        LambdaQueryWrapper<Permission> queryWrapper = Wrappers.lambdaQuery(Permission.class)
                .orderByAsc(Permission::getParentId)
                .orderByAsc(Permission::getNum);
        List<Permission> permissionList = permissionService.list(queryWrapper);
        List<TreeSelectVO> selectList = getTreeSelect(permissionList, 0);
        TreeSelectVO treeSelectVO = new TreeSelectVO();
        treeSelectVO.setId("0");
        treeSelectVO.setLabel("顶级菜单");
        treeSelectVO.setChildren(selectList);
        return Result.success(Collections.singletonList(treeSelectVO));
    }

    public List<TreeSelectVO> getTreeSelect(List<Permission> permissionList, Integer parentId) {
        return permissionList.stream()
                .filter(permission -> permission.getParentId().equals(parentId))
                .map(permission -> {
                    TreeSelectVO treeSelectVO = new TreeSelectVO();
                    treeSelectVO.setId(permission.getPermissionId().toString());
                    treeSelectVO.setLabel(permission.getName());
                    List<TreeSelectVO> treeSelect = getTreeSelect(permissionList, permission.getPermissionId());
                    if(!treeSelect.isEmpty()){
                        treeSelectVO.setChildren(treeSelect);
                    }
                    return treeSelectVO;
                }).collect(Collectors.toList());
    }

    @GetMapping(value = "/tree")
    @ApiOperation(value = "根据角色id查询权限树", notes = "根据角色id查询权限树 (不传角色id代表查询全部)")
    public Result<List<TreeVO>> tree(
            @RequestParam(value = "roleId", required = false) Integer roleId
    ) {
        List<TreeVO> list = permissionService.getTree(roleId);
        return Result.success(list);
    }

    @ApiOperation(value = "查询权限详情", notes = "查询权限详情")
    @GetMapping("/{permissionId}")
    public Result<Permission> query(
            @PathVariable(value = "permissionId") Integer permissionId
    ) {
        Permission permission = permissionService.getById(permissionId);
        return Result.success(permission);
    }

    @PostMapping
    @ApiOperation(value = "添加或修改权限", notes = "添加或修改权限")
    public Result<Void> saveOrUpdate(@RequestBody @Valid Permission permission) {
        redisService.del(REDIS_PERMISSION_TREE);
        boolean b = permissionService.saveOrUpdate(permission);
        return b ? Result.success() : Result.failed();
    }

    @PutMapping(value = "/cache")
    @ApiOperation(value = "更新权限缓存", notes = "更新权限缓存")
    public Result<Void> updatePermissionCache() {
        permissionService.getAndUpdatePermissions();
        permissionService.getAndUpdateTree();
        return Result.success();
    }

    @DeleteMapping("/{permissionIds}")
    @ApiOperation(value = "删除权限", notes = "删除权限")
    public Result<Void> delete(
            @PathVariable(value = "permissionIds") List<Integer> permissionIds
    ) {
        redisService.del(REDIS_PERMISSION_TREE);
        boolean b = permissionService.removeByIds(permissionIds);
        return b ? Result.success() : Result.failed();
    }

}
