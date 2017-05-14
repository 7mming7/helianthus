package com.ha.executor;

import com.ha.config.JobRootConfiguration;
import com.ha.event.type.JobExecutionEvent;
import com.ha.event.type.JobStatusTraceEvent;
import com.ha.exception.JobExecutionEnvironmentException;

import java.util.Collection;

/**
 * 作业内部服务门面服务
 * User: shuiqing
 * DateTime: 17/5/11 下午3:11
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface JobFacade {

    /**
     * 读取作业配置.
     *
     * @param fromCache 是否从缓存中读取
     * @return 作业配置
     */
    JobRootConfiguration loadJobRootConfiguration(boolean fromCache);

    /**
     * 检查作业执行环境.
     *
     * @throws JobExecutionEnvironmentException 作业执行环境异常
     */
    void checkJobExecutionEnvironment() throws JobExecutionEnvironmentException;

    /**
     * 注册作业启动信息.
     *
     * @param jobContexts 分片上下文
     */
    void registerJobBegin(JobContexts jobContexts);

    /**
     * 注册作业完成信息.
     *
     * @param jobContexts 分片上下文
     */
    void registerJobCompleted(JobContexts jobContexts);

    /**
     * 获取当前作业服务器的分片上下文.
     *
     * @return 分片上下文
     */
    JobContexts getJobContexts();

    /**
     * 判断作业是否符合继续运行的条件.
     *
     * <p>如果作业停止或需要重分片或非流式处理则作业将不会继续运行.</p>
     *
     * @return 作业是否符合继续运行的条件
     */
    boolean isEligibleForJobRunning();

    /**
     * 作业执行前的执行的方法.
     *
     * @param jobContexts 分片上下文
     */
    void beforeJobExecuted(JobContexts jobContexts);

    /**
     * 作业执行后的执行的方法.
     *
     * @param jobContexts 分片上下文
     */
    void afterJobExecuted(JobContexts jobContexts);

    /**
     * 发布执行事件.
     *
     * @param jobExecutionEvent 作业执行事件
     */
    void postJobExecutionEvent(JobExecutionEvent jobExecutionEvent);

    /**
     * 发布作业状态追踪事件.
     *
     * @param taskId 作业Id
     * @param state 作业执行状态
     * @param message 作业执行消息
     */
    void postJobStatusTraceEvent(String taskId, JobStatusTraceEvent.State state, String message);
}
