package com.ha.exception;

/**
 * User: shuiqing
 * DateTime: 17/6/1 下午5:06
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class ProjectManagerException extends SystemException {

    private static final long serialVersionUID = 1L;

    public ProjectManagerException(String message) {
        super(message);
    }

    public ProjectManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
