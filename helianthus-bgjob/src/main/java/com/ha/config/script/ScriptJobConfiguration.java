package com.ha.config.script;

import com.ha.api.JobType;
import com.ha.api.script.ScriptJob;
import com.ha.config.JobCoreConfiguration;
import com.ha.config.JobTypeConfiguration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 脚本作业配置
 * User: shuiqing
 * DateTime: 17/5/11 上午9:38
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@RequiredArgsConstructor
@Getter
public class ScriptJobConfiguration implements JobTypeConfiguration {

    private final JobCoreConfiguration coreConfig;

    private final JobType jobType = JobType.SCRIPT;

    private final String jobClass = ScriptJob.class.getCanonicalName();

    private final String scriptCommandLine;
}
