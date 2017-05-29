package com.ha.trigger;

import com.ha.basic.ElasticJob;
import com.ha.exception.JobException;
import org.quartz.*;
import org.quartz.impl.triggers.AbstractTrigger;

import java.text.ParseException;

/**
 * User: shuiqing
 * DateTime: 17/5/27 下午2:25
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class CrondTrigger implements ImportTrigger{
    /**
     * 验证cron表达式的合法性
     */
    private static void validateCron(String cron) {
        if (cron != null && !cron.trim().isEmpty()) {
            try {
                CronExpression.validateExpression(cron.trim());
            } catch (ParseException e) {
                throw new JobException(e);
            }
        }
    }

    public Trigger createTrigger(ElasticJob job){
        String cron = job.getConfigService().getCron();
        validateCron(cron);
        CronScheduleBuilder cronScheduleBuilder;
        if (cron != null && !cron.trim().isEmpty()) {
            cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron.trim());
        } else {
            cronScheduleBuilder = CronScheduleBuilder.cronSchedule("* * * * * ? 2099");
        }
        cronScheduleBuilder = cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
        Trigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(job.getExecutorName() + "_" + job.getJobName())
                .withSchedule(cronScheduleBuilder).build();
        ((AbstractTrigger<CronTrigger>) cronTrigger)
                .setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);

        return cronTrigger;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ImportScheduler build(ElasticJob job) throws SchedulerException{
        ImportScheduler scheduler = new ImportScheduler(job, createTrigger(job));
        scheduler.start();
        return scheduler;
    }

    @Override
    public void retrigger(ImportScheduler scheduler, ElasticJob job) throws SchedulerException {
        scheduler.rescheduleJob(createTrigger(job));
    }
}
