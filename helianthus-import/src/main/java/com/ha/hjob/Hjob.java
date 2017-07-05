package com.ha.hjob;

/**
 * Hjob 所有执行job的接口
 * User: shuiqing
 * DateTime: 17/6/27 下午3:11
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface Hjob {

    String getId();

    String getCommand();

    void run() throws Exception;

    void cancel() throws Exception;

    double getProgress() throws Exception;

    boolean isCanceled();
}
