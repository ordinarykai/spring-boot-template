package com.kai.config;

import com.easy.boot.core.log.WebLog;
import com.easy.boot.core.log.WebLogEvent;
import org.springframework.stereotype.Component;

/**
 * @author kai
 */
@Component
public class CustomWebLogEvent extends WebLogEvent {

    /**
     * 日志入库
     */
    @Override
    public void after(WebLog webLog) {
        // TODO: 2022/5/30 日志入库
    }

}
