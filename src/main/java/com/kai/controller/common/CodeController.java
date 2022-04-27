package com.kai.controller.common;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import com.kai.config.api.Result;
import com.kai.config.api.exception.ApiException;
import com.kai.config.redis.service.RedisService;
import com.kai.bo.dto.SendSmsDTO;
import com.kai.util.sms.ali.AliYunSmsUtil;
import com.kai.util.sms.ali.AliYunTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Objects;

import static com.kai.util.constant.RedisConstant.VERIFY_CODE;

/**
 * @author kai
 * @date 2022/3/12 14:36
 */
@Api(tags = "验证码")
@RestController
@RequestMapping("/api/code")
public class CodeController {

    @Autowired
    private RedisService redisService;

    @GetMapping(value = "/captcha")
    @ApiOperation(value = "获取图形验证码", notes = "获取图形验证码")
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
        session.setMaxInactiveInterval(5 * 60);
        resp.setContentType("image/jpeg");
        resp.setDateHeader("expries", -1);
        resp.setHeader("Cache-Control", "no-cache");
        resp.setHeader("Pragma", "no-cache");
        try (ServletOutputStream outputStream = resp.getOutputStream()) {
            shearCaptcha.write(outputStream);
        }
    }

    @PostMapping(value = "/sms")
    @ApiOperation(value = "发送手机验证码", notes = "发送手机验证码")
    public Result<Void> sendSms(@RequestBody @Valid SendSmsDTO req) {
        String phone = req.getPhone();
        Object preVerifyCode = redisService.get(VERIFY_CODE + phone);
        if (Objects.nonNull(preVerifyCode)) {
            Long expire = redisService.getExpire(VERIFY_CODE + phone);
            // 验证码发送间隔为60秒(前一个验证码的过期时间必须小于4分钟)
            if (expire >= 4 * 60) {
                throw new ApiException("发送过于频繁");
            }
        }
        // 自定义验证码（随机6位数字）
        String verifyCode = new RandomGenerator("0123456789", 6).generate();
        redisService.set(VERIFY_CODE + phone, verifyCode, 5 * 60);
        // 阿里云短信
        LinkedHashMap<String, String> templateParamMap = new LinkedHashMap<>();
        templateParamMap.put("code", verifyCode);
        AliYunSmsUtil.sendSms(AliYunTemplate.COMMON_SMS, phone, templateParamMap);
        return Result.success(null, "发送成功");
    }

}
