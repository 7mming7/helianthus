package com.ha.config.file;

import com.ha.api.JobType;
import com.ha.api.file.FileJob;
import com.ha.config.JobCoreConfiguration;
import com.ha.config.JobTypeConfiguration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 文件作业配置
 * User: shuiqing
 * DateTime: 17/5/11 上午11:34
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@RequiredArgsConstructor
@Getter
public class FileJobConfiguration implements JobTypeConfiguration {

    private final JobCoreConfiguration coreConfig;

    private final JobType jobType = JobType.SCRIPT;

    private final String jobClass = FileJob.class.getCanonicalName();

    private final String filePath;
}
