package com.ha.executor.handler;

import java.util.concurrent.ExecutorService;

/**
 * 线程池服务处理器.
 *
 * <p>用于作业内部的线程池处理使用.</p>
 * User: shuiqing
 * DateTime: 17/5/10 下午3:27
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface ExecutorServiceHandler {

    /**
     * 创建线程池服务对象.
     *
     * @param jobName 作业名
     *
     * @return 线程池服务对象
     */
    ExecutorService createExecutorService(final String jobName);
}
