package com.ha.trigger;

import com.ha.basic.ElasticJob;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * User: shuiqing
 * DateTime: 17/5/27 上午9:41
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class ImportScheduler {

    private static final String SATURN_QUARTZ_WORKER = "-importQuartz-worker";
    private final ElasticJob job;

    private Trigger trigger;
    private final ExecutorService executor;
    private ImportWorker importQuartzWorker;

    public ImportScheduler(final ElasticJob job, final Trigger trigger) {
        this.job = job;
        this.trigger = trigger;
        executor = Executors.newSingleThreadExecutor(new ThreadFactory() {

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, job.getExecutorName() + "_" + job.getConfigService().getJobName() + SATURN_QUARTZ_WORKER);
                if (t.isDaemon()) {
                    t.setDaemon(false);
                }
                if (t.getPriority() != Thread.NORM_PRIORITY) {
                    t.setPriority(Thread.NORM_PRIORITY);
                }
                return t;
            }
        });
    }

    public void start() throws SchedulerException {
        importQuartzWorker = new ImportWorker(job, trigger);
        executor.submit(importQuartzWorker);
    }


    public Trigger getTrigger() {
        return trigger;
    }

    public void shutdown() {
        importQuartzWorker.halt();
        executor.shutdown();
    }

    public void triggerJob() {
        importQuartzWorker.trigger();
    }

    public boolean isShutdown() {
        return importQuartzWorker.isShutDown();
    }

    public void rescheduleJob(Trigger createTrigger) throws SchedulerException {
        this.trigger = createTrigger;
        importQuartzWorker.reInitTrigger(createTrigger);
    }
}
