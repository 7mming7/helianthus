package com.ha.config.simple;

import com.ha.api.JobType;
import com.ha.config.JobCoreConfiguration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 简单作业配置
 * User: shuiqing
 * DateTime: 17/5/10 下午4:27
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@RequiredArgsConstructor
@Getter
public class SimpleJobConfiguration {

    private final JobCoreConfiguration coreConfig;

    private final JobType jobType = JobType.SIMPLE;

    private final String jobClass;
}
