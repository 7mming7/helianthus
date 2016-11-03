package com.ha.config;

import org.apache.commons.configuration.Configuration;

/**
 * User: shuiqing
 * DateTime: 16/10/27 上午11:57
 * Email: helianthus301@163.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface SysConfigAble {

    /**
     * 设置当前系统的配制项
     * @param conf
     */
    void setSysConf(Configuration conf);

    /**
     * 得到配置项
     * @return
     */
    Configuration getSysConf();
}