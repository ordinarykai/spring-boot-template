package com.kai.util.constant;

/**
 * @author kai
 * @date 2022/3/12 13:48
 */
public interface RedisConstant {

    /**
     * 用户登录信息
     */
    String REDIS_LOGIN = "kai:login:";

    /**
     * 手机验证码
     */
    String VERIFY_CODE = "kai:verify_code:";

    /**
     * 用户菜单uri缓存
     */
    String REDIS_PERMISSION = "kai:permission";

    /**
     * 用户角色菜单uri缓存
     */
    String REDIS_PERMISSION_ROLE = "kai:permission:role:";

    /**
     * 用户菜单树缓存
     */
    String REDIS_PERMISSION_TREE = "kai:permission:tree";


}
