package com.ha.hjob;

import com.ha.utils.Props;
import org.slf4j.Logger;

/**
 * No-op job.
 * User: shuiqing
 * DateTime: 17/6/27 下午4:08
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */

public class NoopJob implements Hjob {

    private String jobId;

    public NoopJob(String jobid, Props props, Props jobProps, Logger log) {
        this.jobId = jobid;
    }

    public static String COMMAND = "noop";

    @Override
    public String getCommand() {
        return COMMAND;
    }

    @Override
    public String getId() {
        return this.jobId;
    }

    @Override
    public void run() throws Exception {
    }

    @Override
    public void cancel() throws Exception {
    }

    @Override
    public double getProgress() throws Exception {
        return 0;
    }

    @Override
    public boolean isCanceled() {
        return false;
    }

}
