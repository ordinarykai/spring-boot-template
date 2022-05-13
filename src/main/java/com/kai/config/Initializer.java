package com.kai.config;

import com.kai.service.PermissionService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 程序初始化
 *
 * @author kai
 * @date 2022/3/12 14:02
 */
@Component
public class Initializer {

    @Resource
    private PermissionService permissionService;

    /**
     * 程序初始化
     */
    @PostConstruct
    private void initConfig() {
        permissionService.updatePermissionCache();
        permissionService.updatePermissionTreeCache();
    }

}
