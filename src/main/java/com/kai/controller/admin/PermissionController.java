package com.kai.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kai.bo.vo.PermissionVO;
import com.kai.config.api.Result;
import com.kai.config.redis.service.RedisService;
import com.kai.entity.Permission;
import com.kai.service.PermissionService;
import com.kai.util.bo.TreeSelectVO;
import com.kai.util.bo.TreeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.kai.util.constant.CommonConstant.TOP_PARENT_ID;
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
    @ApiOperation(value = "查询权限列表", notes = "查询权限列表")
    public Result<List<PermissionVO>> list() {
        LambdaQueryWrapper<Permission> queryWrapper = Wrappers.lambdaQuery(Permission.class)
                .orderByAsc(Permission::getParentId)
                .orderByAsc(Permission::getNum);
        List<Permission> permissionList = permissionService.list(queryWrapper);
        List<PermissionVO> permissionVOList = getPermissionVOList(permissionList, TOP_PARENT_ID);
        return Result.success(permissionVOList);
    }

    public List<PermissionVO> getPermissionVOList(List<Permission> permissionList, Integer parentId) {
        return permissionList.stream()
                .filter(permission -> permission.getParentId().equals(parentId))
                .map(permission -> {
                    PermissionVO permissionVO = new PermissionVO();
                    BeanUtils.copyProperties(permission, permissionVO);
                    List<PermissionVO> permissionVOList = getPermissionVOList(permissionList, permission.getPermissionId());
                    permissionVO.setChildren(permissionVOList);
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
        List<TreeSelectVO> selectList = getTreeSelect(permissionList, TOP_PARENT_ID);
        TreeSelectVO treeSelectVO = new TreeSelectVO();
        treeSelectVO.setId(TOP_PARENT_ID);
        treeSelectVO.setLabel("顶级菜单");
        treeSelectVO.setChildren(selectList);
        return Result.success(Collections.singletonList(treeSelectVO));
    }

    public List<TreeSelectVO> getTreeSelect(List<Permission> permissionList, Integer parentId) {
        return permissionList.stream()
                .filter(permission -> permission.getParentId().equals(parentId))
                .map(permission -> {
                    TreeSelectVO treeSelectVO = new TreeSelectVO();
                    treeSelectVO.setId(permission.getPermissionId());
                    treeSelectVO.setLabel(permission.getName());
                    List<TreeSelectVO> treeSelect = getTreeSelect(permissionList, permission.getPermissionId());
                    treeSelectVO.setChildren(treeSelect);
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
