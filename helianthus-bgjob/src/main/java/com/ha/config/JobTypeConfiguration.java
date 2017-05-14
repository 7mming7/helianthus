package com.ha.config;

import com.ha.api.JobType;

/**
 * 作业类型配置
 * User: shuiqing
 * DateTime: 17/5/10 下午3:48
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface JobTypeConfiguration {

    /**
     * 获取作业类型.
     *
     * @return 作业类型
     */
    JobType getJobType();

    /**
     * 获取作业实现类名称.
     *
     * @return 作业实现类名称
     */
    String getJobClass();

    /**
     * 获取作业核心配置.
     *
     * @return 作业核心配置
     */
    JobCoreConfiguration getCoreConfig();
}
