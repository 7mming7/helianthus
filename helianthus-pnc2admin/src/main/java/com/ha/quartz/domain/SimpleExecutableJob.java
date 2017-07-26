package com.ha.quartz.domain;

import org.quartz.JobExecutionContext;

/**
 * User: shuiqing
 * DateTime: 17/4/20 下午3:14
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class SimpleExecutableJob extends AbstractExecutableJob {

    public SimpleExecutableJob(ScheduleJobInfo jobInfo) {
        super(jobInfo);
    }

    @Override
    public boolean execute(JobExecutionContext context) {
        System.out.println(this.toString());
        return true;
    }
}