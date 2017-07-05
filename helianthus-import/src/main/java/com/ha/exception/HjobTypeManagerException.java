package com.ha.exception;

/**
 * User: shuiqing
 * DateTime: 17/7/5 上午10:34
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class HjobTypeManagerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public HjobTypeManagerException(String message) {
        super(message);
    }

    public HjobTypeManagerException(Throwable cause) {
        super(cause);
    }

    public HjobTypeManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
