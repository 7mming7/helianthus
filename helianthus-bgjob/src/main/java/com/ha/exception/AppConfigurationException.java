package com.ha.exception;

/**
 * 作业APP配置异常
 * User: shuiqing
 * DateTime: 17/5/11 下午4:12
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public final class AppConfigurationException extends RuntimeException {

    private static final long serialVersionUID = -1466479389299512371L;

    public AppConfigurationException(final String errorMessage, final Object... args) {
        super(String.format(errorMessage, args));
    }

    public AppConfigurationException(final Throwable cause) {
        super(cause);
    }
}
