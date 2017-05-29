package com.ha.basic;

import com.ha.config.ConfigurationService;
import lombok.Getter;
import lombok.Setter;

/**
 * 分布式作业接口
 * User: shuiqing
 * DateTime: 17/5/22 下午3:57
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Getter
@Setter
public abstract class ElasticJob implements Stopable {

    protected String executorName;

    protected String jobName;

    protected String namespace;

    protected JobScheduler jobScheduler;

    protected ConfigurationService configService;

    public final void execute() {

    }

    protected abstract void executeJob(final ImportExecutionContext importExecutionContext);
}
