package com.ha.exception;

/**
 * User: shuiqing
 * DateTime: 17/8/15 上午11:09
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class HiveQueryException extends Exception {
    private static final long serialVersionUID = 1L;
    private final String query;
    private final int code;
    private final String message;

    public HiveQueryException(String query, int code, String message) {
        this.query = query;
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getQuery() {
        return query;
    }

    @Override
    public String toString() {
        return "HiveQueryException{" + "query='" + query + '\'' + ", code=" + code
                + ", message='" + message + '\'' + '}';
    }
}