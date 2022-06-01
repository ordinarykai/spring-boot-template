package com.kai.constant;

/**
 * 系统常量
 *
 * @author kai
 * @date 2022/3/12 13:49
 */
public interface CommonConstant {

    /**
     * 默认密码
     */
    String DEFAULT_PWD = "123456";

    /**
     * 用户登录过期时间，单位s，默认24小时
     */
    long EXPIRE_TIME = 24 * 60 * 60;

    /**
     * 顶级权限 父类ID 标志
     */
    Integer TOP_PARENT_ID = 0;

}
