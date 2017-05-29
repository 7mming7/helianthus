package com.ha.exception;

/**
 * 作业系统异常
 * User: shuiqing
 * DateTime: 17/5/11 下午4:19
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public final class JobSystemException extends RuntimeException {

    private static final long serialVersionUID = 5018901344199973515L;

    public JobSystemException(final String errorMessage, final Object... args) {
        super(String.format(errorMessage, args));
    }

    public JobSystemException(final Throwable cause) {
        super(cause);
    }
}