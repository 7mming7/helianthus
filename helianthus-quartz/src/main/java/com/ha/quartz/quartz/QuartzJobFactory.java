package com.ha.quartz.quartz;

import com.ha.quartz.model.Constant;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ha.quartz.model.IExecutable;

/**
 * 有状态的Job工厂类
 */
@DisallowConcurrentExecution
public class QuartzJobFactory implements Job {
    private static Logger logger = LoggerFactory.getLogger(QuartzJobFactory.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Object job = context.getMergedJobDataMap().get(Constant.JOB_PARAM_KEY);
        if (job != null && job instanceof IExecutable) {
            if (((IExecutable) job).execute(context))
                logger.info("任务{}执行成功……", job.toString());
            else logger.info("任务{}执行失败！", job.toString());
        }
    }
}
