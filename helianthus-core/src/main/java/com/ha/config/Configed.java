package com.ha.config;

import org.apache.commons.configuration.Configuration;

/**
 * User: shuiqing
 * DateTime: 16/10/27 上午11:56
 * Email: helianthus301@163.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class Configed implements ConfigAble{

    private Configuration conf;


    protected Configed(){
    }

    protected Configed(Configuration conf){
        this.setConf(conf);
    }
    /**
     * @return the conf
     */
    public Configuration getConf() {
        return conf;
    }

    /**
     * @param conf the conf to set
     */
    public void setConf(Configuration conf) {
        this.conf = conf;
    }
}