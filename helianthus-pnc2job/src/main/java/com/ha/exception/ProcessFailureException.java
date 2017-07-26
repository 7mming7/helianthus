package com.ha.exception;

/**
 * User: shuiqing
 * DateTime: 17/7/3 下午3:47
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class ProcessFailureException extends RuntimeException {

    private static final long serialVersionUID = 1;

    private final int exitCode;
    private final String logSnippet;

    public ProcessFailureException(int exitCode, String logSnippet) {
        this.exitCode = exitCode;
        this.logSnippet = logSnippet;
    }

    public int getExitCode() {
        return exitCode;
    }

    public String getLogSnippet() {
        return this.logSnippet;
    }

}
