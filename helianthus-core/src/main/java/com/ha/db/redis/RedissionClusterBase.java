/*
package com.ha.db.redis;

import com.ha.config.HelianthusConfig;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

*/
/**
 * redission 集群 base
 * User: shuiqing
 * DateTime: 17/4/10 下午7:38
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 *//*

public class RedissionClusterBase {

    private static final Logger LOG = LoggerFactory.getLogger(RedissionClusterBase.class);

    private static RedissonClient redissonClient;

    public RedissionClusterBase(){
        Config config = new Config();
        config.useClusterServers().setScanInterval(2000);
        for(String node:HelianthusConfig.getRedisCluster()){
            config.useClusterServers().addNodeAddress(node);
        }

        redissonClient = Redisson.create(config);
    }

    public static RedissonClient getRedissonClient() {
        return redissonClient;
    }

    public static void setRedissonClient(RedissonClient redissonClient) {
        RedissionClusterBase.redissonClient = redissonClient;
    }
}
*/
