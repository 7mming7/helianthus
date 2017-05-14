package com.ha.event.type;

import com.ha.event.JobEvent;
import com.ha.exception.ExceptionUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

/**
 * 作业执行事件
 * User: shuiqing
 * DateTime: 17/5/11 下午4:21
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public final class JobExecutionEvent implements JobEvent {

    private String id = UUID.randomUUID().toString();

    private final String taskId;

    private final String jobName;

    private final ExecutionSource source;

    private Date startTime = new Date();

    @Setter
    private Date completeTime;

    @Setter
    private boolean success;

    @Setter
    private JobExecutionEventThrowable failureCause;

    /**
     * 作业执行成功.
     *
     * @return 作业执行事件
     */
    public JobExecutionEvent executionSuccess() {
        JobExecutionEvent result = new JobExecutionEvent(id, taskId, jobName, source, startTime, completeTime, success, failureCause);
        result.setCompleteTime(new Date());
        result.setSuccess(true);
        return result;
    }

    /**
     * 作业执行失败.
     *
     * @param failureCause 失败原因
     * @return 作业执行事件
     */
    public JobExecutionEvent executionFailure(final Throwable failureCause) {
        JobExecutionEvent result = new JobExecutionEvent(id, taskId, jobName, source, startTime, completeTime, success, new JobExecutionEventThrowable(failureCause));
        result.setCompleteTime(new Date());
        result.setSuccess(false);
        return result;
    }

    /**
     * 获取失败原因.
     *
     * @return 失败原因
     */
    public String getFailureCause() {
        return ExceptionUtil.transform(failureCause == null ? null : failureCause.getThrowable());
    }

    /**
     * 执行来源.
     */
    public enum ExecutionSource {
        NORMAL_TRIGGER, MISFIRE, FAILOVER
    }
}
