package com.kai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kai.util.bo.TreeVO;
import com.kai.bo.vo.PermissionTreeVO;
import com.kai.entity.Permission;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author kai
 * @since 2022-03-12
 */
public interface PermissionService extends IService<Permission> {

    /**
     * 根据角色id查询权限树
     *
     * @param roleId 角色id
     */
    List<TreeVO> getTree(Integer roleId);

    /**
     * 查询权限树并更新缓存
     */
    List<TreeVO> getAndUpdateTree();

    /**
     * 查询所有权限并更新缓存
     */
    List<String> getAndUpdatePermissions();

    /**
     * 查询所有权限
     */
    List<String> getPermissions();

    /**
     * 根据角色id集合查询权限
     *
     * @param roleIds 角色id集合
     */
    Set<String> getPermissionRole(List<Integer> roleIds);

    /**
     * 根据角色id查询权限并更新缓存
     *
     * @param roleId 角色id
     */
    List<String> getAndUpdatePermissionRole(Integer roleId);

}
