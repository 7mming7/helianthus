package com.ha.event;

import com.ha.executor.ExecuteStatus;

/**
 * User: shuiqing
 * DateTime: 17/7/11 下午4:58
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class BlockingStatus {
    private static final long WAIT_TIME = 5 * 60 * 1000;
    private final int execId;
    private final String jobId;
    private ExecuteStatus status;

    public BlockingStatus(int execId, String jobId, ExecuteStatus initialStatus) {
        this.execId = execId;
        this.jobId = jobId;
        this.status = initialStatus;
    }

    public ExecuteStatus blockOnFinishedStatus() {
        if (status == null) {
            return null;
        }

        while (!ExecuteStatus.isStatusFinished(status)) {
            synchronized (this) {
                try {
                    this.wait(WAIT_TIME);
                } catch (InterruptedException e) {
                }
            }
        }

        return status;
    }

    public ExecuteStatus viewStatus() {
        return this.status;
    }

    public void unblock() {
        synchronized (this) {
            this.notifyAll();
        }
    }

    public void changeStatus(ExecuteStatus status) {
        synchronized (this) {
            this.status = status;
            if (ExecuteStatus.isStatusFinished(status)) {
                unblock();
            }
        }
    }

    public int getExecId() {
        return execId;
    }

    public String getJobId() {
        return jobId;
    }
}
