package com.ha.quartz.model;

import org.quartz.JobExecutionContext;

public interface IExecutable {

    /**
     * 执行某个操作
     * @return
     * @param context
     */
    boolean execute(JobExecutionContext context);
}
