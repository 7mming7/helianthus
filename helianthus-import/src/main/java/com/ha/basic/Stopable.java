package com.ha.basic;

/**
 * 可停止的作业或目标
 * User: shuiqing
 * DateTime: 17/5/26 下午6:10
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface Stopable {

    /**
     * 停止运行中的作业或目标.
     */
    void stop();

    /**
     * 中止作业（上报作业状态）.
     */
    void forceStop();

    /**
     * 恢复运行作业或目标.
     */
    void resume();

    /**
     * ZK断开、Executor停止时关闭作业（不上报状态）
     */
    void abort();

    /**
     * 关闭作业
     */
    void shutdown();
}
