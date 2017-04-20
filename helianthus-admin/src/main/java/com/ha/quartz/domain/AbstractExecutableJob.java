package com.ha.quartz.domain;

import java.io.Serializable;

/**
 * 抽象可执行定时任务
 * User: shuiqing
 * DateTime: 17/4/20 下午2:32
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public abstract class AbstractExecutableJob implements IExecutable,Serializable {

    private ScheduleJobInfo jobInfo;

    public AbstractExecutableJob(ScheduleJobInfo jobInfo) {
        this.jobInfo = jobInfo;
    }

    public ScheduleJobInfo getJobInfo() {
        return jobInfo;
    }

    public void setJobInfo(ScheduleJobInfo jobInfo) {
        this.jobInfo = jobInfo;
    }

    public String getJobName() {
        return jobInfo.getJobName();
    }

    public String getJobGroup() {
        return jobInfo.getJobGroup();
    }

    public String getCronExpression() {
        return jobInfo.getCronExpression();
    }
}
