package com.kai.service;

import com.kai.bo.dto.SendSmsDTO;
import com.kai.bo.vo.CaptchaVO;

import java.io.IOException;

/**
 * @author kai
 * @date 2022/3/12 14:36
 */
public interface CodeServe {

    /**
     * 获取图形验证码
     */
    CaptchaVO getCaptchaCode() throws IOException;

    /**
     * 发送手机验证码
     */
    void sendSmsCode(SendSmsDTO dto);

}
