package com.ha.hjob;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: shuiqing
 * DateTime: 17/6/27 下午5:38
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public abstract class AbstractJob implements Hjob {

    private final Logger _log = LoggerFactory.getLogger(AbstractJob.class);

    public static final String JOB_ID = "job.id";
    public static final String JOB_TYPE = "type";
    public static final String JOB_CLASS = "job.class";

    private final String _id;

    private volatile double _progress;

    protected AbstractJob(String id) {
        _id = id;
        _progress = 0.0;
    }

    public String getId() {
        return _id;
    }

    public double getProgress() throws Exception {
        return _progress;
    }

    public void setProgress(double progress) {
        this._progress = progress;
    }

    public void cancel() throws Exception {
        throw new RuntimeException("Job " + _id + " does not support cancellation!");
    }

    public Logger getLog() {
        return this._log;
    }

    public void debug(String message) {
        this._log.debug(message);
    }

    public void debug(String message, Throwable t) {
        this._log.debug(message, t);
    }

    public void info(String message) {
        this._log.info(message);
    }

    public void info(String message, Throwable t) {
        this._log.info(message, t);
    }

    public void warn(String message) {
        this._log.warn(message);
    }

    public void warn(String message, Throwable t) {
        this._log.warn(message, t);
    }

    public void error(String message) {
        this._log.error(message);
    }

    public void error(String message, Throwable t) {
        this._log.error(message, t);
    }

    public abstract void run() throws Exception;

    public boolean isCanceled() {
        return false;
    }
}