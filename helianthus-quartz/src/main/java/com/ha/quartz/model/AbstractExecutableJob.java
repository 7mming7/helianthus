package com.ha.quartz.model;

import java.io.Serializable;

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
