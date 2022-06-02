package com.kai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easy.boot.core.redis.service.RedisService;
import com.easy.boot.core.util.bo.TreeVO;
import com.kai.entity.Permission;
import com.kai.mapper.PermissionMapper;
import com.kai.service.PermissionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.kai.constant.RedisConstant.REDIS_PERMISSION;
import static com.kai.constant.RedisConstant.REDIS_PERMISSION_TREE;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author kai
 * @since 2022-03-12
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Resource
    private RedisService redisService;

    @Override
    public List<TreeVO> updatePermissionTreeCache() {
        List<Permission> permissionList = this.list();
        List<Permission> parentPermissionList = permissionList
                .stream()
                .filter(permission -> permission.getParentId() == 0)
                .collect(Collectors.toList());
        permissionList.removeAll(parentPermissionList);
        List<TreeVO> permissionTree = createPermissionTree(parentPermissionList, permissionList);
        redisService.set(REDIS_PERMISSION_TREE, permissionTree);
        return permissionTree;
    }

    @Override
    public List<String> updatePermissionCache() {
        List<String> permissions = this.list()
                .stream().map(Permission::getUri)
                .collect(Collectors.toList());
        redisService.set(REDIS_PERMISSION, permissions);
        return permissions;
    }

    @Override
    public List<String> getPermissions() {
        if (redisService.hasKey(REDIS_PERMISSION)) {
            return redisService.get(REDIS_PERMISSION);
        }
        return updatePermissionCache();
    }

    private List<TreeVO> createPermissionTree(List<Permission> parentPermissionList, List<Permission> permissionList) {
        return parentPermissionList.stream().map(permission -> {
            Integer permissionId = permission.getPermissionId();
            List<Permission> childPermissionList = permissionList
                    .stream()
                    .filter(childPermission -> childPermission.getParentId().equals(permissionId))
                    .collect(Collectors.toList());
            permissionList.removeAll(childPermissionList);
            TreeVO res = new TreeVO();
            res.setValue(permission.getPermissionId().toString());
            res.setLabel(permission.getName());
            BeanUtils.copyProperties(permission, res);
            List<TreeVO> children = createPermissionTree(childPermissionList, permissionList);
            res.setChildren(children);
            return res;
        }).collect(Collectors.toList());
    }

}
