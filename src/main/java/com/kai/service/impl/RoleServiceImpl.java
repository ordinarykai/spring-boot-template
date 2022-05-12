package com.kai.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kai.bo.dto.RoleAddDTO;
import com.kai.bo.dto.RolePageDTO;
import com.kai.bo.dto.RoleUpdateDTO;
import com.kai.bo.vo.RoleVO;
import com.kai.config.api.Result;
import com.kai.config.api.exception.ApiException;
import com.kai.config.redis.service.RedisService;
import com.kai.entity.Admin;
import com.kai.entity.Role;
import com.kai.mapper.RoleMapper;
import com.kai.service.AdminService;
import com.kai.service.PermissionService;
import com.kai.service.RoleService;
import com.kai.util.bo.SelectVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.kai.util.constant.CommonConstant.DISABLE;
import static com.kai.util.constant.CommonConstant.ENABLE;
import static com.kai.util.constant.RedisConstant.REDIS_PERMISSION_ROLE;

/**
 * @author kai
 * @date 2022/3/12 20:20
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private AdminService adminService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RedisService redisService;

    @Override
    public List<SelectVO> select() {
        List<Role> roleList = this.list(Wrappers.lambdaQuery(Role.class)
                .eq(Role::getStatus, ENABLE));
        return roleList.stream().map(role -> {
            SelectVO res = new SelectVO();
            res.setLabel(role.getRoleName());
            res.setValue(role.getRoleId().toString());
            return res;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean add(RoleAddDTO req) {
        int count = this.count(Wrappers.lambdaQuery(Role.class)
                .eq(Role::getRoleName, req.getRoleName().trim()));
        if (count > 0) {
            throw new ApiException("该角色名称已存在，请重新录入");
        }
        Role role = new Role();
        BeanUtils.copyProperties(req, role);
        boolean b = role.insert();
        if (b) {
            // 根据角色id查询权限并更新缓存
            permissionService.updateRolePermissionCache(role.getRoleId());
        }
        return b;
    }

    @Override
    public boolean update(RoleUpdateDTO dto) {
        int count = this.count(Wrappers.lambdaQuery(Role.class)
                .eq(Role::getRoleName, dto.getRoleName().trim())
                .ne(Role::getRoleId, dto.getRoleId()));
        if (count > 0) {
            throw new ApiException("该角色名称已存在，请重新录入");
        }
        Role role = this.getById(dto.getRoleId());
        BeanUtils.copyProperties(dto, role);
        boolean b = role.updateById();
        if (b) {
            // 根据角色id查询权限并更新缓存
            permissionService.updateRolePermissionCache(role.getRoleId());
        }
        return b;
    }

    @Override
    public boolean delete(List<Integer> roleIds) {
        int count = adminService.count(new LambdaQueryWrapper<Admin>()
                .in(Admin::getRoleId, roleIds));
        if (count > 0) {
            throw new ApiException("该角色绑定了用户，请先删除用户");
        }
        boolean b = this.removeByIds(roleIds);
        if (b) {
            // 删除角色对应权限缓存
            List<String> keys = roleIds.stream()
                    .map(roleId -> REDIS_PERMISSION_ROLE + roleId)
                    .collect(Collectors.toList());
            redisService.del(keys);
        }
        return b;
    }

    @Override
    public IPage<RoleVO> page(RolePageDTO req) {
        String roleName = req.getRoleName();
        String status = req.getStatus();
        LambdaQueryWrapper<Role> queryWrapper = Wrappers.lambdaQuery(Role.class);
        queryWrapper.like(StringUtils.isNotBlank(roleName), Role::getRoleName, roleName);
        queryWrapper.like(StringUtils.isNotBlank(status), Role::getStatus, status);
        queryWrapper.orderByDesc(Role::getUpdateTime).orderByDesc(Role::getCreateTime);
        Page<Role> page = this.page(new Page<>(req.getPage(), req.getLimit()), queryWrapper);
        return page.convert(role -> {
            RoleVO res = new RoleVO();
            BeanUtils.copyProperties(role, res);
            return res;
        });
    }

    @Override
    public RoleVO query(Integer roleId) {
        Role role = this.getById(roleId);
        RoleVO res = new RoleVO();
        if (Objects.nonNull(role)) {
            BeanUtils.copyProperties(role, res);
        }
        return res;
    }

    @Override
    public Result<Void> updateStatus(Integer roleId) {
        Role role = this.getById(roleId);
        Assert.notNull(role, "系统异常，要操作的角色信息不存在");
        Integer oldStatus = role.getStatus();
        if (ENABLE.equals(oldStatus)) {
            role.setStatus(DISABLE);
        } else {
            role.setStatus(ENABLE);
        }
        boolean b = role.updateById();
        if (b && DISABLE.equals(oldStatus)) {
            return Result.success(null, "启用成功");
        }
        if (!b && DISABLE.equals(oldStatus)) {
            return Result.failed("启用失败");
        }
        if (b && ENABLE.equals(oldStatus)) {
            return Result.success(null, "禁用成功");
        }
        return Result.failed("禁用失败");
    }

}
