package com.ha.quartz.domain;

import org.quartz.JobExecutionContext;

/**
 * quartz 执行是否可执行接口
 * User: shuiqing
 * DateTime: 17/4/20 下午2:23
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface IExecutable {

    /**
     * 执行某个操作
     * @param context
     * @return
     */
    boolean execute(JobExecutionContext context);
}
