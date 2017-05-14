package com.ha.api.simple;

import com.ha.api.ElasticJob;
import com.ha.api.JobContext;

/**
 * 简单分布式作业接口
 * User: shuiqing
 * DateTime: 17/5/9 下午3:12
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface SimpleJob extends ElasticJob {

    /**
     * 执行作业.
     *
     * @param jobContext 上下文
     */
    void execute(JobContext jobContext);
}