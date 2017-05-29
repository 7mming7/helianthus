package com.ha.trigger;

import com.ha.basic.ElasticJob;
import com.ha.basic.ImportConstants;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.spi.OperableTrigger;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * User: shuiqing
 * DateTime: 17/5/26 下午6:08
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Slf4j
public class ImportWorker implements Runnable {
    private ElasticJob job;
    private OperableTrigger triggerObj;
    private final Object sigLock = new Object();
    private boolean paused = false;
    private boolean triggered = false;
    private AtomicBoolean halted = new AtomicBoolean(false);

    public ImportWorker(ElasticJob job, Trigger trigger) throws SchedulerException {
        this.job = job;
        initTrigger(trigger);
    }

    public void reInitTrigger(Trigger trigger) throws SchedulerException {
        initTrigger(trigger);
        synchronized (sigLock) {
            sigLock.notifyAll();
        }
    }

    private void initTrigger(Trigger trigger) throws SchedulerException {
        if(trigger == null) return;

        this.triggerObj = (OperableTrigger) trigger;
        Date ft = this.triggerObj.computeFirstFireTime(null);
        if (ft == null) {
            throw new SchedulerException(
                    "Based on configured schedule, the given trigger '" + trigger.getKey() + "' will never fire.");
        }
    }


    public boolean isShutDown() {
        return halted.get();
    }

    void togglePause(boolean pause) {
        synchronized (sigLock) {
            paused = pause;
            sigLock.notifyAll();
        }
    }

    void halt() {
        synchronized (sigLock) {
            halted.set(true);
            sigLock.notifyAll();
        }
    }

    void trigger() {
        synchronized (sigLock) {
            triggered = true;
            sigLock.notifyAll();
        }
    }

    @Override
    public void run() {
        while (!halted.get()) {
            try {
                synchronized (sigLock) {
                    while (paused && !halted.get()) {
                        try {
                            sigLock.wait(1000L);
                        } catch (InterruptedException ignore) {
                        }
                    }
                    if (halted.get()) {
                        break;
                    }
                }
                long timeUntilTrigger = 1000;
                if(triggerObj != null){
                    triggerObj.updateAfterMisfire(null);
                    long now = System.currentTimeMillis();
                    long triggerTime = triggerObj.getNextFireTime().getTime();
                    timeUntilTrigger = triggerTime - now;
                }

                while (timeUntilTrigger > 2) {
                    synchronized (sigLock) {
                        if (halted.get()) {
                            break;
                        }
                        if (triggered){
                            break;
                        }

                        try {
                            sigLock.wait(timeUntilTrigger);
                        } catch (InterruptedException ignore) {
                        }

                        if(triggerObj != null){
                            long now = System.currentTimeMillis();
                            timeUntilTrigger = triggerObj.getNextFireTime().getTime() - now;
                        }
                    }
                }
                boolean goAhead = true;
                // 触发执行只有两个条件：1.时间到了；2。点立即执行；
                synchronized (sigLock) {
                    goAhead = !halted.get() && !paused;
                    // 重置立即执行标志；
                    if (triggered) {
                        triggered = false;
                    } else	if (goAhead && triggerObj != null) {
                        // 更新执行时间
                        triggerObj.triggered(null);
                    }
                }
                if (goAhead) {
                    job.execute();
                }

            } catch (RuntimeException e) {
                log.error(String.format(ImportConstants.ERROR_LOG_FORMAT, job.getJobName(), e.getMessage()), e);
            }
        }

    }

}
