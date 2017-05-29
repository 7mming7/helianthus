package com.ha.basic;

import java.util.HashMap;
import java.util.Map;

/**
 * 任务类型管理器
 * User: shuiqing
 * DateTime: 17/5/26 下午3:57
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class JobTypeManager {

    private static JobTypeManager instance = new JobTypeManager();

    public static JobTypeManager getInstance() {
        return instance;
    }

    private Map<String, Class<? extends ElasticJob>> handlerMap = new HashMap<String, Class<? extends ElasticJob>>();

    private JobTypeManager() {
    }

    public void registerHandler(String jobType, Class<? extends ElasticJob> jobClazz) {
        handlerMap.put(jobType, jobClazz);
    }

    public Class<? extends ElasticJob> getHandler(String jobType) {
        return handlerMap.get(jobType);
    }
}