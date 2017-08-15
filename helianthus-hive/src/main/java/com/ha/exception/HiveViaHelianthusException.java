package com.ha.exception;

/**
 * User: shuiqing
 * DateTime: 17/8/14 下午5:00
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class HiveViaHelianthusException extends Exception{
    private static final long serialVersionUID = 1L;

    public HiveViaHelianthusException(String s) {
        super(s);
    }

    public HiveViaHelianthusException(Exception e) {
        super(e);
    }

    public HiveViaHelianthusException(String s, Exception e) {
        super(s, e);
    }
}
