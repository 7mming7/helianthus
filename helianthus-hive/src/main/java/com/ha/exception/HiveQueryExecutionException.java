package com.ha.exception;

/**
 * User: shuiqing
 * DateTime: 17/8/14 下午5:11
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class HiveQueryExecutionException extends Exception{
    private static final long serialVersionUID = 1L;

    /**
     * Query that caused the failure.
     */
    private final String query;

    /**
     * Error code defined by Hive
     */
    private final int returnCode;

    public HiveQueryExecutionException(int returnCode, String query) {
        this.returnCode = returnCode;
        this.query = query;
    }

    public String getLine() {
        return query;
    }

    public int getReturnCode() {
        return returnCode;
    }

    @Override
    public String toString() {
        return "HiveQueryExecutionException{" + "query='" + query + '\''
                + ", returnCode=" + returnCode + '}';
    }
}
