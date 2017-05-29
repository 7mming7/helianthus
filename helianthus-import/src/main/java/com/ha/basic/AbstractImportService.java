package com.ha.basic;

import com.ha.config.JobConfiguration;
import lombok.Getter;
import lombok.Setter;

/**
 * 调度基础服务
 * User: shuiqing
 * DateTime: 17/5/27 上午9:48
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Getter
@Setter
public abstract class AbstractImportService implements Shutdownable{

    protected String jobName;

    protected JobScheduler jobScheduler;

    protected JobConfiguration jobConfiguration;

    public AbstractImportService(final JobScheduler jobScheduler) {
        this.jobScheduler = jobScheduler;
        this.jobName = jobScheduler.getJobName();
        jobConfiguration = jobScheduler.getCurrentConf();
    }

    /**
     * 服务启用
     */
    public void start(){

    }

    @Override
    public void shutdown() {
    }
}
