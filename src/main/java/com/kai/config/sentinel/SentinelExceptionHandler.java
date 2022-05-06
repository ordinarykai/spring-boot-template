package com.kai.config.sentinel;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.kai.config.api.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Sentinel异常处理
 * @author kai
 */
@Slf4j
@RestControllerAdvice
@ConditionalOnProperty(value = "spring.cloud.sentinel.enabled", havingValue = "true")
public class SentinelExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = BlockException.class)
    public Result<Void> blockExceptionHandler() {
        return Result.failed("Blocked by Sentinel (flow limiting)");
    }

}
