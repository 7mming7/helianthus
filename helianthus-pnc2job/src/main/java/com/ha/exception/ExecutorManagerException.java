package com.ha.exception;

import com.ha.executor.ExecutableFlow;

/**
 * User: shuiqing
 * DateTime: 17/7/12 下午5:09
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class ExecutorManagerException extends Exception {
    public enum Reason {
        SkippedExecution
    }

    private static final long serialVersionUID = 1L;
    private ExecutableFlow flow = null;
    private Reason reason = null;

    public ExecutorManagerException(Exception e) {
        super(e);
    }

    public ExecutorManagerException(String message) {
        super(message);
    }

    public ExecutorManagerException(String message, ExecutableFlow flow) {
        super(message);
        this.flow = flow;
    }

    public ExecutorManagerException(String message, Reason reason) {
        super(message);
        this.reason = reason;
    }

    public ExecutorManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExecutableFlow getExecutableFlow() {
        return flow;
    }

    public Reason getReason() {
        return reason;
    }
}