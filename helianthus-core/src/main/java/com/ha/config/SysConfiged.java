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
public class SysConfiged implements SysConfigAble{

    private Configuration conf;


    protected SysConfiged(){
    }

    protected SysConfiged(Configuration conf){
        this.setSysConf(conf);
    }

    @Override
    public void setSysConf(Configuration conf) {
        this.conf = conf;
    }

    @Override
    public Configuration getSysConf() {
        return conf;
    }
}