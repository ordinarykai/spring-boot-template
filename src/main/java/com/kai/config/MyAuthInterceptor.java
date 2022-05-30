package com.kai.config;

import com.easy.boot.core.auth.AuthInterceptor;
import com.easy.boot.core.auth.AuthProperties;
import com.easy.boot.core.auth.AuthUtil;
import com.kai.service.PermissionService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * 用户认证授权拦截器
 *
 * @author kai
 * @date 2022/3/12 14:21
 */
@Component
public class MyAuthInterceptor extends AuthInterceptor {

    @Resource
    private PermissionService permissionService;

    public MyAuthInterceptor(AuthProperties authProperties) {
        super(authProperties);
    }

    @Override
    public Set<String> getPresentUserPermissions() {
        List<Integer> roleIds = AuthUtil.get().getRoleIds();
        return permissionService.getPermissionRole(roleIds);
    }

    @Override
    public List<String> getAllPermissions() {
        return permissionService.getPermissions();
    }

}
