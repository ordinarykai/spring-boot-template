package com.kai.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kai.bo.dto.RoleAddDTO;
import com.kai.bo.dto.RolePageDTO;
import com.kai.bo.dto.RoleUpdateDTO;
import com.kai.bo.vo.RoleVO;
import com.kai.config.api.Result;
import com.kai.entity.Role;
import com.kai.util.bo.SelectVO;

import java.util.List;

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

}