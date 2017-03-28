package com.ha.base;

/**
 * 全局常量
 * User: shuiqing
 * DateTime: 17/3/5 下午1:21
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface GlobalConstants {

    // ================================
    /* 1-任务正在运行
       2-任务完成
       3-任务失败
       4-任务被kill掉
       5-任务延期
     */
    public static final int JOB_RUNNING = 1;
    public static final int JOB_SUCCESS = 2;
    public static final int JOB_ERROR = 3;
    public static final int JOB_KILL = 4;
    public static final int JOB_DELAY = 5;
    // ================================
}
