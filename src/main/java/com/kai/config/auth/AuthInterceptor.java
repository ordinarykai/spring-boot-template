package com.kai.config.auth;

import com.alibaba.fastjson.JSONObject;
import com.kai.config.api.ResultCode;
import com.kai.service.PermissionService;
import com.kai.util.LoginUtil;
import com.kai.util.bo.LoginInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import static com.kai.util.constant.CommonConstant.TOKEN;

/**
 * 用户认证授权拦截器
 *
 * @author kai
 * @date 2022/3/12 14:21
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Resource
    private PermissionService permissionService;
    /**
     * 路径匹配器
     */
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException {
        String uri = request.getRequestURI();
        String token = request.getHeader(TOKEN);
        // token为空，认证失败
        if (StringUtils.isBlank(token)) {
            return failed(response, ResultCode.UNAUTHORIZED);
        }
        LoginInfo loginInfo = LoginUtil.get(token);
        // token对应redis缓存无数据，认证失败
        if (loginInfo == null) {
            return failed(response, ResultCode.UNAUTHORIZED);
        }
        // 判断登录用户是否有权限访问该路径
        List<String> allPermissions = permissionService.getPermissions();
        if (allPermissions.contains(uri) && !hasPermissions(loginInfo.getRoleIds(), uri)) {
            return failed(response, ResultCode.FORBIDDEN);
        }
        return true;
    }

    /**
     * 判断登录用户是否有权限访问该uri
     *
     * @param roleIds 登录用户角色id集合
     * @param uri         请求uri
     * @return true：有权限，false：没有授权
     */
    private boolean hasPermissions(List<Integer> roleIds, String uri) {
        Set<String> permissions = permissionService.getPermissionRole(roleIds);
        return permissions.stream().anyMatch(pattern -> antPathMatcher.match(pattern, uri));
    }

    /**
     * 认证授权失败，返回提示信息
     */
    private boolean failed(HttpServletResponse response, ResultCode resultCode) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        JSONObject res = new JSONObject();
        res.put("code", resultCode.getCode());
        res.put("message", resultCode.getMessage());
        PrintWriter out = response.getWriter();
        out.append(res.toString());
        return false;
    }

}
