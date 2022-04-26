package com.kai;

import cn.hutool.core.io.FileUtil;
import com.kai.entity.Permission;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 权限数据生成工具类
 *
 * @author kai
 */
public class PermissionGenerator {

    private static final String CONTROLLER_PACKAGE = "com.kai.controller";
    private static final String SQL_FILE_NAME = "D:\\permission.sql";
    private static final String FORMAT_SQL = "INSERT INTO `t_permission`" +
            "(`parent_id`, `type`, `level`, `name`, `uri`, `icon`, `num`, `create_time`, `update_time`) " +
            "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')";

    public static void main(String[] args) {
        executeAutoGenerator();
    }

    public static void executeAutoGenerator() throws BeansException {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(true);
        Set<BeanDefinition> candidateComponents = provider.findCandidateComponents(CONTROLLER_PACKAGE);
        List<Permission> permissions = new ArrayList<>();
        candidateComponents.forEach((beanDefinition) -> {
            String className = beanDefinition.getBeanClassName();
            assert className != null;
            className = className.split("\\$\\$")[0];
            try {
                Class<?> clazz = Class.forName(className);
                RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                for (Method method : clazz.getDeclaredMethods()) {
                    Permission permission = new Permission();
                    permission.setUri(requestMapping.value().length == 0 ? "" : requestMapping.value()[0]);
                    ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
                    permission.setName(apiOperation == null ? "" : apiOperation.value());
                    if (method.isAnnotationPresent(GetMapping.class)) {
                        GetMapping getMapping = method.getAnnotation(GetMapping.class);
                        permission.setUri(permission.getUri() + (getMapping.value().length == 0 ? "" : getMapping.value()[0]) + ":get");
                    } else if (method.isAnnotationPresent(PostMapping.class)) {
                        PostMapping postMapping = method.getAnnotation(PostMapping.class);
                        permission.setUri(permission.getUri() + (postMapping.value().length == 0 ? "" : postMapping.value()[0]) + ":post");
                    } else if (method.isAnnotationPresent(PutMapping.class)) {
                        PutMapping putMapping = method.getAnnotation(PutMapping.class);
                        permission.setUri(permission.getUri() + (putMapping.value().length == 0 ? "" : putMapping.value()[0]) + ":put");
                    } else if (method.isAnnotationPresent(DeleteMapping.class)) {
                        DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
                        permission.setUri(permission.getUri() + (deleteMapping.value().length == 0 ? "" : deleteMapping.value()[0]) + ":delete");
                    } else if (method.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping requestMappingMethod = method.getAnnotation(RequestMapping.class);
                        permission.setUri(permission.getUri() + (requestMappingMethod.value().length == 0 ? "" : requestMappingMethod.value()[0]) + ":" + requestMappingMethod.method()[0].name());
                    } else {
                        System.err.println(className + "." + method.getName() + "方法不是接口");
                        continue;
                    }
                    permission.setUri(permission.getUri().replace("//", "/"));
                    permissions.add(permission);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        String insertSql = generateSql(permissions);
        System.err.println("====================接口权限sql如下====================");
        System.err.println(insertSql);
        FileUtil.writeBytes(insertSql.getBytes(StandardCharsets.UTF_8), FileUtil.touch(SQL_FILE_NAME));
    }

    private static String generateSql(Collection<Permission> permissions) {
        return permissions.stream().map(permission ->
                String.format(FORMAT_SQL, -1, 2, 3, permission.getName(), permission.getUri(), "", 0, LocalDateTime.now(), LocalDateTime.now()))
                .collect(Collectors.joining(";\n"));
    }

}

