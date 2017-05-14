package com.ha.api.dataflow;

import com.ha.api.ElasticJob;
import com.ha.api.JobContext;

import java.util.List;

/**
 * 数据流分布式作业接口
 * User: shuiqing
 * DateTime: 17/5/10 上午10:07
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface DataflowJob<T> extends ElasticJob{

    /**
     * 获取待处理数据.
     *
     * @param jobContext 分片上下文
     * @return 待处理的数据集合
     */
    List<T> fetchData(JobContext jobContext);

    /**
     * 处理数据.
     *
     * @param jobContext 分片上下文
     * @param data 待处理数据集合
     */
    void processData(JobContext jobContext, List<T> data);
}
