package com.ha.executor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * 上下文集合
 * User: shuiqing
 * DateTime: 17/5/11 下午3:06
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@RequiredArgsConstructor
@Getter
@ToString
public final class JobContexts implements Serializable {

    private static final long serialVersionUID = -4585977349142082152L;

    /**
     * 作业任务ID.
     */
    private final String taskId;

    /**
     * 作业名称.
     */
    private final String jobName;

    /**
     * 作业自定义参数.
     * 可以配置多个相同的作业, 但是用不同的参数作为不同的调度实例.
     */
    private final String jobParameter;

    /**
     * 作业事件采样统计数.
     */
    private int jobEventSamplingCount;

    /**
     * 当前作业事件采样统计数.
     */
    @Setter
    private int currentJobEventSamplingCount;

    /**
     * 是否允许可以发送作业事件.
     */
    @Setter
    private boolean allowSendJobEvent = true;

    public JobContexts(final String taskId, final String jobName, final String jobParameter,
                             final int jobEventSamplingCount) {
        this.taskId = taskId;
        this.jobName = jobName;
        this.jobParameter = jobParameter;
        this.jobEventSamplingCount = jobEventSamplingCount;
    }
}
