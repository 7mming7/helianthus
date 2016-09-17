package com.ha.quartz.model;

import org.quartz.JobExecutionContext;

public class SimpleExecutableJob extends AbstractExecutableJob  {

    public SimpleExecutableJob(ScheduleJobInfo jobInfo) {
        super(jobInfo);
    }

    @Override
    public boolean execute(JobExecutionContext context) {
        /**
         * 这里做一些操作
         */
        System.out.println(this.toString());
        return true;
    }
}
