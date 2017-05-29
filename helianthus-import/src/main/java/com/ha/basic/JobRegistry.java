package com.ha.basic;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 作业注册表
 * User: shuiqing
 * DateTime: 17/5/26 下午5:16
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class JobRegistry {

    private static ConcurrentHashMap<String, JobScheduler> SCHEDULER_MAP = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String,Object> JOB_BUSINESS_INSTANCE_MAP = new ConcurrentHashMap<String, Object>();

    private JobRegistry() {
    }

    public static ConcurrentHashMap<String, JobScheduler> getSchedulerMap() {
        return SCHEDULER_MAP;
    }

    /**
     * 添加作业控制器.
     *
     */
    public static void addJobScheduler(final String jobName, final JobScheduler jobScheduler) {
        SCHEDULER_MAP.put(jobName, jobScheduler);
    }

    public static void clearJob(String jobName) {
        if (SCHEDULER_MAP != null) {
            JobScheduler jobScheduler = SCHEDULER_MAP.remove(jobName);
            if(jobScheduler != null && jobScheduler.getJob()!=null){
                jobScheduler.getJob().shutdown();
            }
        }
    }

    private static String getKey( String jobName) {
        return new StringBuilder(100).append(jobName).toString();
    }

    public static void addJobBusinessInstance(String jobName, Object jobBusinessInstance) {
        JOB_BUSINESS_INSTANCE_MAP.putIfAbsent(getKey(jobName), jobBusinessInstance);
    }

    public static Object getJobBusinessInstance( String jobName) {
        return JOB_BUSINESS_INSTANCE_MAP.get(getKey(jobName));
    }
}
