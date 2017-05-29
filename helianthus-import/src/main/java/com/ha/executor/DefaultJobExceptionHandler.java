package com.ha.executor;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认作业异常处理器
 * User: shuiqing
 * DateTime: 17/5/10 下午3:29
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Slf4j
public class DefaultJobExceptionHandler implements JobExceptionHandler {

    @Override
    public void handleException(final String jobName, final Throwable cause) {
        log.error(String.format("Job '%s' exception occur in job processing", jobName), cause);
    }
}
