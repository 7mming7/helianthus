package com.ha.executor;

import com.ha.api.JobContext;
import com.ha.config.JobRootConfiguration;
import com.ha.event.type.JobExecutionEvent;
import com.ha.event.type.JobStatusTraceEvent;
import com.ha.exception.JobExecutionEnvironmentException;
import com.ha.exception.JobSystemException;
import com.ha.executor.handler.ExecutorServiceHandler;
import com.ha.executor.handler.ExecutorServiceHandlerRegistry;
import com.ha.executor.handler.JobExceptionHandler;
import com.ha.executor.handler.JobProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * 弹性分布式作业执行器
 * User: shuiqing
 * DateTime: 17/5/11 下午2:57
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Slf4j
public abstract class AbstractElasticJobExecutor {
    @Getter(AccessLevel.PROTECTED)
    private final JobFacade jobFacade;

    @Getter(AccessLevel.PROTECTED)
    private final JobRootConfiguration jobRootConfig;

    private final String jobName;

    private final ExecutorService executorService;

    private final JobExceptionHandler jobExceptionHandler;

    protected AbstractElasticJobExecutor(final JobFacade jobFacade) {
        this.jobFacade = jobFacade;
        jobRootConfig = jobFacade.loadJobRootConfiguration(true);
        jobName = jobRootConfig.getTypeConfig().getCoreConfig().getJobName();
        executorService = ExecutorServiceHandlerRegistry.getExecutorServiceHandler(jobName, (ExecutorServiceHandler) getHandler(JobProperties.JobPropertiesEnum.EXECUTOR_SERVICE_HANDLER));
        jobExceptionHandler = (JobExceptionHandler) getHandler(JobProperties.JobPropertiesEnum.JOB_EXCEPTION_HANDLER);
    }

    private Object getHandler(final JobProperties.JobPropertiesEnum jobPropertiesEnum) {
        String handlerClassName = jobRootConfig.getTypeConfig().getCoreConfig().getJobProperties().get(jobPropertiesEnum);
        try {
            Class<?> handlerClass = Class.forName(handlerClassName);
            if (jobPropertiesEnum.getClassType().isAssignableFrom(handlerClass)) {
                return handlerClass.newInstance();
            }
            return getDefaultHandler(jobPropertiesEnum, handlerClassName);
        } catch (final ReflectiveOperationException ex) {
            return getDefaultHandler(jobPropertiesEnum, handlerClassName);
        }
    }

    private Object getDefaultHandler(final JobProperties.JobPropertiesEnum jobPropertiesEnum, final String handlerClassName) {
        log.warn("Cannot instantiation class '{}', use default '{}' class.", handlerClassName, jobPropertiesEnum.getKey());
        try {
            return Class.forName(jobPropertiesEnum.getDefaultValue()).newInstance();
        } catch (final ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new JobSystemException(e);
        }
    }

    /**
     * 执行作业.
     */
    public final void execute() {
        try {
            jobFacade.checkJobExecutionEnvironment();
        } catch (final JobExecutionEnvironmentException cause) {
            jobExceptionHandler.handleException(jobName, cause);
        }
        JobContexts shardingContexts = jobFacade.getJobContexts();
        if (shardingContexts.isAllowSendJobEvent()) {
            jobFacade.postJobStatusTraceEvent(shardingContexts.getTaskId(), JobStatusTraceEvent.State.TASK_STAGING, String.format("Job '%s' execute begin.", jobName));
        }
        try {
            jobFacade.beforeJobExecuted(shardingContexts);
            //CHECKSTYLE:OFF
        } catch (final Throwable cause) {
            //CHECKSTYLE:ON
            jobExceptionHandler.handleException(jobName, cause);
        }
        execute(shardingContexts, JobExecutionEvent.ExecutionSource.NORMAL_TRIGGER);
        try {
            jobFacade.afterJobExecuted(shardingContexts);
            //CHECKSTYLE:OFF
        } catch (final Throwable cause) {
            //CHECKSTYLE:ON
            jobExceptionHandler.handleException(jobName, cause);
        }
    }

    private void execute(final JobContexts shardingContexts, final JobExecutionEvent.ExecutionSource executionSource) {

        jobFacade.registerJobBegin(shardingContexts);
        String taskId = shardingContexts.getTaskId();
        if (shardingContexts.isAllowSendJobEvent()) {
            jobFacade.postJobStatusTraceEvent(taskId, JobStatusTraceEvent.State.TASK_RUNNING, "");
        }
        try {
            process(shardingContexts, executionSource);
        } finally {
            // TODO 考虑增加作业失败的状态，并且考虑如何处理作业失败的整体回路
            jobFacade.registerJobCompleted(shardingContexts);

        }
    }

    private void process(final JobContexts shardingContexts, final JobExecutionEvent.ExecutionSource executionSource) {
        final JobExecutionEvent jobExecutionEvent =  new JobExecutionEvent(shardingContexts.getTaskId(), jobName, executionSource);
        process(shardingContexts, jobExecutionEvent);
        if (executorService.isShutdown()) {
            return;
        }
        executorService.submit(new Runnable() {

            @Override
            public void run() {
                try {
                    process(shardingContexts, jobExecutionEvent);
                } finally {

                }
            }
        });
    }

    private void process(final JobContexts jobContexts, final JobExecutionEvent startEvent) {
        if (jobContexts.isAllowSendJobEvent()) {
            jobFacade.postJobExecutionEvent(startEvent);
        }
        log.trace("Job '{}' executing.", jobName);
        JobExecutionEvent completeEvent;
        try {
            process(new JobContext(jobContexts));
            completeEvent = startEvent.executionSuccess();
            log.trace("Job '{}' executed.", jobName);
            if (jobContexts.isAllowSendJobEvent()) {
                jobFacade.postJobExecutionEvent(completeEvent);
            }
            // CHECKSTYLE:OFF
        } catch (final Throwable cause) {
            // CHECKSTYLE:ON
            completeEvent = startEvent.executionFailure(cause);
            jobFacade.postJobExecutionEvent(completeEvent);
            jobExceptionHandler.handleException(jobName, cause);
        }
    }

    protected abstract void process(JobContext jobContext);
}
