package com.ha.config;

/**
 * 作业配置根接口
 * User: shuiqing
 * DateTime: 17/5/10 下午4:05
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface JobRootConfiguration {

    /**
     * 获取作业类型配置.
     *
     * @return 作业类型配置
     */
    JobTypeConfiguration getTypeConfig();
}
