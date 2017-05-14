package com.ha.api;

import com.ha.executor.JobContexts;
import lombok.Getter;

/**
 * 任务上下文
 * User: shuiqing
 * DateTime: 17/5/9 下午3:55
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Getter
public final class JobContext {

    /**
     * 作业名称.
     */
    private String jobName;

    /**
     * 作业任务ID.
     */
    private String taskId;

    /**
     * 作业自定义参数.
     * 可以配置多个相同的作业, 但是用不同的参数作为不同的调度实例.
     */
    private String jobParameter;

    public JobContext(final JobContexts jobContexts) {
        jobName = jobContexts.getJobName();
        taskId = jobContexts.getTaskId();
        jobParameter = jobContexts.getJobParameter();
    }
}
