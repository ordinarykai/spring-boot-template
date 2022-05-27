package com.kai.config;

import com.kai.boot.auth.AuthInterceptor;
import com.kai.boot.util.LoginUtil;
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
@Component("authInterceptor")
public class MyAuthInterceptor extends AuthInterceptor {

    @Resource
    private PermissionService permissionService;
    @Resource
    private LoginUtil loginUtil;

    public MyAuthInterceptor(LoginUtil loginUtil) {
        super(loginUtil);
    }

    @Override
    public Set<String> getPresentUserPermissions() {
        List<Integer> roleIds = loginUtil.get().getRoleIds();
        return permissionService.getPermissionRole(roleIds);
    }

    @Override
    public List<String> getAllPermissions() {
        return permissionService.getPermissions();
    }

}
