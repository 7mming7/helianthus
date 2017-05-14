package com.ha.api.file;

import com.ha.api.ElasticJob;
import com.ha.api.JobContext;

import java.io.File;
import java.util.List;

/**
 * 文件处理分布式作业
 * User: shuiqing
 * DateTime: 17/5/10 上午10:34
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface FileJob extends ElasticJob{

    /**
     * 获取待处理文件.
     *
     * @param jobContext 分片上下文
     * @return 待处理的文件
     */
    File fetchFile(JobContext jobContext);

    /**
     * 处理数据.
     *
     * @param jobContext 分片上下文
     * @param file 待处理文件
     */
    void processFile(JobContext jobContext, File file);
}
