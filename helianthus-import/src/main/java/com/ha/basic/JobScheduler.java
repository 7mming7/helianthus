package com.ha.basic;

import com.ha.config.ConfigurationService;
import com.ha.config.JobConfiguration;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;

/**
 * 作业调度器
 * User: shuiqing
 * DateTime: 17/5/26 下午5:18
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Getter
@Setter
@Slf4j
public class JobScheduler {

    private String jobName;

    private ElasticJob job;

    private final JobConfiguration currentConf;

    private final ConfigurationService configService;

    public JobScheduler(final JobConfiguration jobConfiguration){
        this.currentConf = jobConfiguration;
        JobRegistry.addJobScheduler(jobName, this);

        configService = new ConfigurationService(this);
    }

    private void createJob() throws SchedulerException {
        Class<?> jobClass = currentConf.getImportJobClass();
        try {
            job = (ElasticJob) jobClass.newInstance();
        } catch (Exception e) {
            log.error(String.format(ImportConstants.ERROR_LOG_FORMAT, jobName, "createJobException:"), e);
            throw new RuntimeException("can not create job with job type " + currentConf.getJobType());
        }
        job.setJobScheduler(this);
        job.setConfigService(configService);

        job.setJobName(jobName);
    }

    /**
     * 初始化作业.
     */
    public void init() {
        try {
            String currentConfJobName = currentConf.getJobName();
            log.info("[{}] msg=Elastic job: job controller init, job name is: {}.", jobName, currentConfJobName);

            startAll();
            createJob();
        } catch (Throwable t) {
            log.error(String.format(ImportConstants.ERROR_LOG_FORMAT, jobName, t.getMessage()), t);
            shutdown(false);
        }
    }

    private void startAll() {
        configService.start();
    }

    /**
     * 关闭调度器.
     */
    public void shutdown(boolean removejob) {
        try {
            if (job != null) {
                job.shutdown();
                Thread.sleep(500);
            }
        } catch (final Exception e) {
            log.error(String.format(ImportConstants.ERROR_LOG_FORMAT, jobName, e.getMessage()), e);
        }

        configService.shutdown();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        JobRegistry.clearJob(jobName);
    }
}
