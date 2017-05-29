package com.ha.basic;

import com.ha.config.JobConfiguration;
import lombok.Getter;
import lombok.Setter;

/**
 * 作业运行上下文
 * User: shuiqing
 * DateTime: 17/5/27 下午2:34
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Getter
@Setter
public class ImportExecutionContext {

    /**
     * 作业配置类
     */
    private JobConfiguration jobConfiguration;

    private Class<?> jobClass;
}
