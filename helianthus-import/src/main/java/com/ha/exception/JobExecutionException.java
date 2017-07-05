package com.ha.exception;

/**
 * User: shuiqing
 * DateTime: 17/7/3 下午4:01
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class JobExecutionException extends RuntimeException {

    private final static long serialVersionUID = 1;

    public JobExecutionException(String message) {
        super(message);
    }

    public JobExecutionException(Throwable cause) {
        super(cause);
    }

    public JobExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

}
