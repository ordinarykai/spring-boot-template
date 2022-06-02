package com.kai.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.easy.boot.core.api.Result;
import com.easy.boot.core.util.bo.SelectVO;
import com.easy.boot.core.util.bo.TreeVO;
import com.kai.dto.RoleAddDTO;
import com.kai.dto.RolePageDTO;
import com.kai.dto.RoleUpdateDTO;
import com.kai.dto.RoleVO;
import com.kai.entity.Role;

import java.util.List;
import java.util.Set;

/**
 * @author kai
 * @date 2022/3/12 20:19
 */
public interface RoleService extends IService<Role> {

    /**
     * 查询角色下拉框
     */
    List<SelectVO> select();

    /**
     * 添加角色
     */
    boolean add(RoleAddDTO req);


    /**
     * 根据角色id更新角色权限缓存
     *
     * @param roleId 角色id
     * @return 对应角色的权限
     */
    List<String> updateRolePermissionCache(Integer roleId);

    /**
     * 根据角色id集合查询权限
     *
     * @param roleIds 角色id集合
     * @return 对应角色的权限
     */
    Set<String> getPermissionRole(List<Integer> roleIds);

    /**
     * 编辑角色
     */
    boolean update(RoleUpdateDTO req);

    /**
     * 删除角色
     */
    boolean delete(List<Integer> roleIds);

    /**
     * 查询角色分页列表
     */
    IPage<RoleVO> page(RolePageDTO req);

    /**
     * 查询角色详情
     */
    RoleVO query(Integer roleId);

    /**
     * 启用/禁用角色
     */
    Result<Void> updateStatus(Integer roleId);

    /**
     * 根据角色id查询权限树
     *
     * @param roleId 角色id
     * @return 权限树
     */
    List<TreeVO> getTree(Integer roleId);

}