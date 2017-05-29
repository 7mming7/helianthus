package com.ha.exception;

/**
 * 分布式作业异常基类
 * User: shuiqing
 * DateTime: 17/5/27 下午2:25
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class JobException extends RuntimeException {
    private static final long serialVersionUID = -5323792555332165319L;

    public JobException(final String errorMessage, final Object... args) {
        super(String.format(errorMessage, args));
    }

    public JobException(final Exception cause) {
        super(cause);
    }

    public JobException(Throwable cause) {
        super(cause);
    }
}
