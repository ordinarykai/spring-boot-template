package com.kai.controller.common;

import com.kai.bo.dto.SendSmsDTO;
import com.kai.config.api.Result;
import com.kai.service.CodeServe;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * @author kai
 * @date 2022/3/12 14:36
 */
@Api(tags = "验证码")
@RestController
@RequestMapping("/api/code")
public class CodeController {

    @Resource
    private CodeServe codeServe;

    @GetMapping(value = "/captcha")
    @ApiOperation(value = "获取图形验证码", notes = "获取图形验证码")
    public void getCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        codeServe.getCode(req, resp);
    }

    @PostMapping(value = "/sms")
    @ApiOperation(value = "发送手机验证码", notes = "发送手机验证码")
    public Result<Void> sendSms(@RequestBody @Valid SendSmsDTO dto) {
        codeServe.sendSms(dto);
        return Result.success(null, "发送成功");
    }

}
