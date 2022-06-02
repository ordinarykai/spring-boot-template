package com.kai.controller.common;

import com.easy.boot.core.api.Result;
import com.kai.dto.CaptchaVO;
import com.kai.dto.SendSmsDTO;
import com.kai.service.CodeServe;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;

/**
 * @author kai
 * @date 2022/3/12 14:36
 */
@Api(tags = "验证码")
@RestController
@RequestMapping("/code")
public class CodeController {

    @Resource
    private CodeServe codeServe;

    @GetMapping(value = "/captcha")
    @ApiOperation(value = "获取图形验证码", notes = "获取图形验证码")
    public Result<CaptchaVO> getCaptchaCode() throws IOException {
        CaptchaVO captchaVO = codeServe.getCaptchaCode();
        return Result.success(captchaVO);
    }

    @PostMapping(value = "/sms")
    @ApiOperation(value = "发送手机验证码", notes = "发送手机验证码")
    public Result<Void> sendSmsCode(@RequestBody @Valid SendSmsDTO dto) {
        codeServe.sendSmsCode(dto);
        return Result.success(null, "发送成功");
    }

}
