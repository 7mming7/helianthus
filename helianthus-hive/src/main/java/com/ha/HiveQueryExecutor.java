package com.ha;

import com.ha.exception.HiveQueryExecutionException;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * User: shuiqing
 * DateTime: 17/8/14 下午5:16
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface HiveQueryExecutor {

    /**
     * Execute the specified quer(y|ies).
     *
     * @param q Query to be executed. Queries may include \n and mutliple,
     *          ;-delimited statements. The entire string is passed to Hive.
     *
     * @throws HiveQueryExecutionException if Hive cannont execute a query.
     */
    void executeQuery(String q) throws HiveQueryExecutionException;

    /**
     * Redirect the query execution's stdout
     *
     * @param out
     */
    void setOut(PrintStream out);

    /**
     * Redirect the query execution's stdin
     *
     * @param in
     */
    void setIn(InputStream in);

    /**
     * Redirect the query execution's stderr
     *
     * @param err
     */
    void setErr(PrintStream err);
}
