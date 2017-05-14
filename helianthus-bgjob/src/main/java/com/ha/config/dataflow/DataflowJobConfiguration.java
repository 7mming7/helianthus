package com.ha.config.dataflow;

import com.ha.api.JobType;
import com.ha.config.JobCoreConfiguration;
import com.ha.config.JobTypeConfiguration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 数据作业配置
 * User: shuiqing
 * DateTime: 17/5/11 上午11:36
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@RequiredArgsConstructor
@Getter
public class DataflowJobConfiguration implements JobTypeConfiguration {

    private final JobCoreConfiguration coreConfig;

    private final JobType jobType = JobType.DATAFLOW;

    private final String jobClass;

    private final boolean streamingProcess;
}
