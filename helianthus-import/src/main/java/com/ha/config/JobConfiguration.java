package com.ha.config;

import com.ha.basic.ElasticJob;
import com.ha.basic.JobTypeManager;
import lombok.Getter;
import lombok.Setter;

/**
 * 作业配置信息
 * User: shuiqing
 * DateTime: 17/5/26 下午5:30
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Getter
@Setter
public class JobConfiguration {

    private Class<? extends ElasticJob> importJobClass;

    public JobConfiguration(String jobName) {
        this.jobName = jobName;
    }

    public Class<? extends ElasticJob> getImportJobClass(){
        //测试时可以直接设置实现类
        if(importJobClass != null){
            return importJobClass;
        }

        return JobTypeManager.getInstance().getHandler(getJobType());
    }

    /**
     * 作业名称.
     */
    private final String jobName;

    private String jobClass="";

    /**
     * 作业启动时间的cron表达式.
     */
    private String cron;

    /**
     * 作业自定义参数.
     *
     * <p>
     * 可以配置多个相同的作业, 但是用不同的参数作为不同的调度实例.
     * </p>
     */
    private String jobParameter = "";

    /**
     * 作业描述信息.
     */
    private String description = "";

    /**
     * 作业类型, 动态判断(非配置)
     */
    private String jobType;
}
