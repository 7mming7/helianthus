package com.ha.executor;

import java.util.concurrent.ExecutorService;

/**
 * 默认的线程池服务处理器
 * User: shuiqing
 * DateTime: 17/5/10 下午3:37
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public final class DefaultExecutorServiceHandler implements ExecutorServiceHandler {

    @Override
    public ExecutorService createExecutorService(final String jobName) {
        return new ExecutorServiceObject("inner-job-" + jobName, Runtime.getRuntime().availableProcessors() * 2).createExecutorService();
    }
}
