package com.kai.service;

import com.kai.bo.dto.SendSmsDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author kai
 * @date 2022/3/12 14:36
 */
public interface CodeServe {

    /**
     * 获取图形验证码
     */
    void getCode(HttpServletRequest req,
                 HttpServletResponse resp) throws IOException;

    /**
     * 发送手机验证码
     */
    void sendSms(SendSmsDTO dto);

}
