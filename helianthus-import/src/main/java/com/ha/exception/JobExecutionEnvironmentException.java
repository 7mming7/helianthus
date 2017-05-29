package com.ha.exception;

/**
 * 作业执行环境异常
 * User: shuiqing
 * DateTime: 17/5/11 下午3:12
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public final class JobExecutionEnvironmentException extends Exception {

    private static final long serialVersionUID = -6670738108926897433L;

    public JobExecutionEnvironmentException(final String errorMessage, final Object... args) {
        super(String.format(errorMessage, args));
    }
}
