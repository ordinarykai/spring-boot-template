package com.kai.config.api.exception;

import com.kai.config.api.ResultCode;

/**
 * 未授权异常
 *
 * @author kai
 * @date 2022/3/12 12:28
 */
public class ForbiddenException extends ApiException {

    public ForbiddenException() {
        super(ResultCode.FORBIDDEN);
    }

    public ForbiddenException(String message) {
        super(ResultCode.FORBIDDEN, message);
    }

}
