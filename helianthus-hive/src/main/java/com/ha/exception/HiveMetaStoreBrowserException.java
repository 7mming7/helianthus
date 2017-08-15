package com.ha.exception;

/**
 * Thrown when unexpected Hive metastore browsing problems come up
 * User: shuiqing
 * DateTime: 17/8/14 下午5:31
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */

public class HiveMetaStoreBrowserException extends Exception {
    private static final long serialVersionUID = 1L;

    public HiveMetaStoreBrowserException(String msg) {
        super(msg);
    }

    public HiveMetaStoreBrowserException(Throwable t) {
        super(t);
    }

    public HiveMetaStoreBrowserException(String msg, Throwable t) {
        super(msg, t);
    }
}