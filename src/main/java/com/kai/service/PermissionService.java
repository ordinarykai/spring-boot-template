package com.kai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easy.boot.core.util.bo.TreeVO;
import com.kai.entity.Permission;

import java.util.List;

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
     * 更新权限树缓存
     *
     * @return 权限树
     */
    List<TreeVO> updatePermissionTreeCache();

    /**
     * 更新权限缓存
     *
     * @return 所有权限
     */
    List<String> updatePermissionCache();

    /**
     * 查询所有权限
     *
     * @return 所有权限
     */
    List<String> getPermissions();

}
