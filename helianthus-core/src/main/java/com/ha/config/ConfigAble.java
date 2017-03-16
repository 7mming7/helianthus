package com.ha.config;

import org.apache.commons.configuration.Configuration;

/**
 * User: shuiqing
 * DateTime: 16/10/29 上午11:21
 * Email: helianthus301@163.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface ConfigAble {

    void setConf(Configuration conf);

    Configuration getConf();
}