package com.kai.util;

import com.kai.config.api.exception.UnauthorizedException;
import com.kai.config.redis.service.RedisService;
import com.kai.util.constant.RedisConstant;
import com.kai.util.bo.LoginInfo;
import org.springframework.util.StringUtils;

import java.util.Objects;

import static com.kai.Application.applicationContext;
import static com.kai.util.constant.CommonConstant.EXPIRE_TIME;
import static com.kai.util.constant.CommonConstant.TOKEN;

/**
 * @author kai
 * @date 2022/3/12 13:39
 */
public class LoginUtil {

    private static final RedisService redisService = applicationContext.getBean(RedisService.class);

    /**
     * 存储用户信息
     */
    public static String set(LoginInfo loginInfo) {
        String token = StringUtil.getUuid();
        redisService.set(RedisConstant.REDIS_LOGIN + token, loginInfo, EXPIRE_TIME);
        return token;
    }

    /**
     * 获取用户信息
     *
     * @param needLogin 是否需要登录 true:是 false:否
     */
    public static LoginInfo get(boolean needLogin) {
        LoginInfo loginInfo = get();
        if (needLogin && Objects.isNull(loginInfo)) {
            throw new UnauthorizedException();
        }
        return loginInfo;
    }

    /**
     * 获取用户信息
     */
    public static LoginInfo get() {
        String token = ServletUtil.getRequest().getHeader(TOKEN);
        return get(token);
    }

    /**
     * 获取用户信息
     */
    public static LoginInfo get(String token) {
        if (!StringUtils.hasText(token)) {
            return null;
        }
        LoginInfo loginInfo = redisService.get(RedisConstant.REDIS_LOGIN + token);
        if (Objects.nonNull(loginInfo)) {
            redisService.expire(RedisConstant.REDIS_LOGIN + token, EXPIRE_TIME);
        }
        return loginInfo;
    }

    /**
     * 踢出用户
     */
    public static void out() {
        out(ServletUtil.getRequest().getHeader(TOKEN));
    }

    /**
     * 踢出指定用户
     *
     * @param token 登录token令牌
     */
    public static void out(String token) {
        String key = RedisConstant.REDIS_LOGIN + token;
        if (StringUtils.hasText(token) && redisService.hasKey(key)) {
            redisService.del(key);
        }
    }

}
