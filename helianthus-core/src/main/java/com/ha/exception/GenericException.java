package com.ha.exception;

/**
 * 所有程序可预料异常的基类。这个异常可以在controller层作为能被捕获从而获取向最终用户报错信息的异常
 * User: shuiqing
 * DateTime: 17/4/26 上午11:49
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class GenericException extends RuntimeException {

    private static final long serialVersionUID = -3663305095578458646L;

    public GenericException() {
        super();
    }

    /**
     * @param message
     */
    public GenericException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public GenericException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public GenericException(String message, Throwable cause) {
        super(message, cause);
    }
}
