package com.kai.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import com.kai.bo.dto.SendSmsDTO;
import com.kai.config.api.exception.ApiException;
import com.kai.config.redis.service.RedisService;
import com.kai.service.CodeServe;
import com.kai.util.sms.ali.AliYunSmsUtil;
import com.kai.util.sms.ali.AliYunTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Objects;

import static com.kai.util.constant.RedisConstant.VERIFY_CODE;

/**
 * @author kai
 * @date 2022/3/12 14:36
 */
@Service
public class CodeServeImpl implements CodeServe {

    /***
     * 验证码有效期
     */
    int expireTime = 5 * 60;
    /**
     * 验证码发送间隔
     */
    int intervalTime = 60;

    @Resource
    private RedisService redisService;

    @Override
    public void getCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //自定义验证码（随机4位字母+数字，排除了O/o/0）
        String baseNumber = "123456789";
        String baseCapitalLetter = "ABCDEFGHIJKLMNPQRSTUVWXY";
        String baseSmallLetter = "abcdefghijklmnpqrstuvwxy";
        RandomGenerator randomGenerator = new RandomGenerator(baseCapitalLetter +
                baseSmallLetter + baseNumber, 4);
        //设置验证码宽高和干扰线条数
        LineCaptcha shearCaptcha = CaptchaUtil.createLineCaptcha(80, 25, 4, 15);
        shearCaptcha.setGenerator(randomGenerator);
        //将验证码存入session,过期时间设为5分钟
        HttpSession session = req.getSession();
        session.setAttribute("verifyCode", shearCaptcha.getCode());
        session.setMaxInactiveInterval(expireTime);
        resp.setContentType("image/jpeg");
        resp.setDateHeader("expries", -1);
        resp.setHeader("Cache-Control", "no-cache");
        resp.setHeader("Pragma", "no-cache");
        try (ServletOutputStream outputStream = resp.getOutputStream()) {
            shearCaptcha.write(outputStream);
        }
    }

    @Override
    public void sendSms(SendSmsDTO dto) {
        String phone = dto.getPhone();
        Object preVerifyCode = redisService.get(VERIFY_CODE + phone);
        if (Objects.nonNull(preVerifyCode)) {
            if (expireTime - redisService.getExpire(VERIFY_CODE + phone) <= intervalTime) {
                throw new ApiException("发送过于频繁");
            }
        }
        // 自定义验证码（随机6位数字）
        String verifyCode = new RandomGenerator("0123456789", 6).generate();
        redisService.set(VERIFY_CODE + phone, verifyCode, expireTime);
        // 阿里云短信
        LinkedHashMap<String, String> templateParamMap = new LinkedHashMap<>();
        templateParamMap.put("code", verifyCode);
        AliYunSmsUtil.sendSms(AliYunTemplate.COMMON_SMS, phone, templateParamMap);
    }

}
