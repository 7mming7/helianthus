package com.ha.exception;

/**
 * 系统级别的错误。这类错误往往可以预料，但是无法处理。比如，运行中网络出错导致系统运行异常。这类错误往往
 * 不需要修改程序，但是需要重启服务器来解决
 * User: shuiqing
 * DateTime: 17/4/26 上午11:48
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class SystemException extends GenericException {
    private static final long serialVersionUID = -1711029265248495640L;

    public SystemException() {
        super();
    }

    public SystemException(String message) {
        super(message);
    }

    public SystemException(Throwable cause) {
        super(cause);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

}
