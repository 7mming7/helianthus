package com.ha.executor;

/**
 * 作业异常处理器
 * User: shuiqing
 * DateTime: 17/5/10 下午3:26
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface JobExceptionHandler {

    /**
     * 处理作业异常.
     *
     * @param jobName 作业名称
     * @param cause 异常原因
     */
    void handleException(String jobName, Throwable cause);
}