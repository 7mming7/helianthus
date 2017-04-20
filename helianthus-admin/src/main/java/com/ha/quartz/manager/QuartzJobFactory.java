package com.ha.quartz.manager;

import com.ha.quartz.base.QuartzConstants;
import com.ha.quartz.domain.IExecutable;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 有状态的状态工厂类
 * User: shuiqing
 * DateTime: 17/4/20 下午3:18
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class QuartzJobFactory implements Job {

    private static Logger logger = LoggerFactory.getLogger(QuartzJobFactory.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Object job = context.getMergedJobDataMap().get(QuartzConstants.JOB_PARAM_KEY);
        if (job != null && job instanceof IExecutable) {
            if (((IExecutable) job).execute(context))
                logger.info("任务{}执行成功……", job.toString());
            else logger.info("任务{}执行失败！", job.toString());
        }
    }
}
