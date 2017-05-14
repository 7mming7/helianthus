package com.ha.event.type;

import com.ha.context.ExecutionType;
import com.ha.event.JobEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

/**
 * 作业状态痕迹事件
 * User: shuiqing
 * DateTime: 17/5/11 下午3:16
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
public final class JobStatusTraceEvent implements JobEvent {

    private String id = UUID.randomUUID().toString();

    private final String jobName;

    @Setter
    private String originalTaskId = "";

    private final String taskId;

    private final String slaveId;

    private final Source source;

    private final ExecutionType executionType;

    private final String shardingItems;

    private final State state;

    private final String message;

    private Date creationTime = new Date();

    public enum State {
        TASK_STAGING, TASK_RUNNING, TASK_FINISHED, TASK_KILLED, TASK_LOST, TASK_FAILED, TASK_ERROR
    }

    public enum Source {
        CLOUD_SCHEDULER, CLOUD_EXECUTOR, LITE_EXECUTOR
    }
}
