package com.ha.db.redis;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.ha.config.HelianthusConfig;
import com.ha.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.io.IOException;
import java.util.*;

/**
 * User: shuiqing
 * DateTime: 17/3/27 上午11:00
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class RedisClusterBase {
    private final static Logger LOG = LoggerFactory.getLogger(RedisClusterBase.class);
    private Map<String, JedisPool> nodeMap;
    private TreeMap<Long, String> slotHostMap;
    private Set<HostAndPort> clusterNodes;
    private JedisCluster jedisCluster;

    public RedisClusterBase(HelianthusConfig config) {
        clusterNodes = Sets.newHashSet();
        for (String val : config.getRedisCluster()) {
            String t[] = val.split(":");
            clusterNodes.add(new HostAndPort(t[0], Integer.parseInt(t[1])));
        }
        jedisCluster = new JedisCluster(clusterNodes);
        init();
    }

    public Object handRedisClusetData(Object object, RedisOperateType redisOperateType) {
        try {
            if (object == null)
                return null;
            switch (redisOperateType) {
                case GET_KV:
                    if (object instanceof List) {
                        List<String> keyList = (List<String>) object;
                        return get(keyList);
                    } else if (object instanceof String) {
                        return jedisCluster.get(object.toString());
                    }
                    break;
                case SET_KV:
                    if (object instanceof Map) {
                        Map<String, String> map = (Map<String, String>) object;
                        set(map);
                    }
                    break;
                case GET_HM:
                    if (object instanceof Map) {
                        Map<String, List<String>> map = (Map<String, List<String>>) object;
                        return hmget(map);
                    }
                    break;
                case SET_HM:
                    if (object instanceof Map) {
                        Map<String, Map<String, String>> map = (Map<String, Map<String, String>>) object;
                        set(map);
                    }
                    break;
                default:
                    return null;
            }
        } catch (Exception e) {
            LOG.error("handRedisClusetData error:" + e.getMessage(), e);
            if (!checkAndInitClusterState()) {// 如果redis状态问题，则等待一直到问题解决
                init();
                return handRedisClusetData(object, redisOperateType);
            }
        }
        return null;
    }


    /**
     * 功能：数据读取（适用于key-value形式）
     * @param keys 键值集合
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    private Map<String, String> get(List<String> keys) throws IOException {
        Map<String, String> result = new HashMap<String, String>();
        if (keys == null || keys.size() == 0)
            return result;
        Map<String, Response<String>> response = new HashMap<String, Response<String>>();
        JedisClusterPipeline pipeline = new JedisClusterPipeline(nodeMap, slotHostMap);
        for (String key : keys) {
            if (key == null || key.length() == 0)
                continue;
            response.put(key, pipeline.get(key));
        }
        pipeline.sync();
        for (Map.Entry<String, Response<String>> en : response.entrySet()) {
            String key = en.getKey();
            String val = en.getValue().get();
            if (val != null) {
                result.put(key, val);
            }
        }
        pipeline.close();
        return result;
    }

    /**
     * 功能：数据读取（适用于hash-map形式）
     * @param keyFields 键值集合
     * @return
     */
    private Map<String, Map<String, String>> hmget(Map<String, List<String>> keyFields) {
        Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
        if (keyFields == null || keyFields.size() == 0)
            return result;
        Map<String, Response<List<String>>> response = Maps.newHashMap();
        JedisClusterPipeline pipeline = new JedisClusterPipeline(nodeMap, slotHostMap);
        for (String key : keyFields.keySet()) {
            if (key == null || key.length() == 0)
                continue;
            response.put(key, pipeline.hmget(key, keyFields.get(key).toArray(new String[] {})));
        }
        pipeline.sync();
        for (Map.Entry<String, Response<List<String>>> en : response.entrySet()) {
            String key = en.getKey();
            List<String> list = en.getValue().get();

            Map<String, String> map = result.get(key);
            if(map==null){
                map=Maps.newHashMap();
                result.put(key, map);
            }
            List<String> fields = keyFields.get(key);
            for (int i = 0; i < fields.size(); i++) {
                String val = list.get(i);
                if (val != null) {
                    map.put(fields.get(i), val);
                }
            }
        }
        pipeline.close();
        return result;
    }

    /**
     * 功能：数据读取（适用于key-value形式）
     * @param <T>
     * @param map 键值集合
     * @return
     * @throws InterruptedException
     */
    private <T> void set(Map<String, T> map) {
        if (map == null || map.size() == 0)
            return;
        JedisClusterPipeline pipeline = new JedisClusterPipeline(nodeMap, slotHostMap);
        for (String key : map.keySet()) {
            if (key == null || key.length() == 0)
                continue;
            T value = map.get(key);
            if(value instanceof Map){
                pipeline.hmset(key, (Map<String, String>) value);
            }else {
                pipeline.set(key, String.valueOf(value));
            }
        }
        pipeline.sync();
        pipeline.close();
    }

    /**
     * 函数名：init
     * 功能描述：初始化方法
     */
    private void init() {
        while (!checkAndInitClusterState()) {
            try {
                Thread.sleep(1000);
                LOG.info("RedisClusterBase init sleep 1 second!");
            } catch (InterruptedException e) {
                LOG.warn("RedisClusterBase:init", e);
            }
        }
    }

    /**
     * 函数名：getRedisClusterState
     * 功能描述：获取redis集群状态
     * @return
     */
    private boolean checkAndInitClusterState() {
        try {
            jedisCluster = new JedisCluster(clusterNodes);
            nodeMap = jedisCluster.getClusterNodes();
            if (nodeMap == null || nodeMap.size() == 0) {
                LOG.info("checkAndInitClusterState 1 nodeMap is null");
                return false;
            }
            //对slotHostMap赋值(将集群中的卡槽信息保存在slotHostMap中)
            for (String node : nodeMap.keySet()) {
                this.slotHostMap = getSlotHostMap(node);
                if (this.slotHostMap == null)
                    continue;
                if (this.slotHostMap.size() > 0) {
                    break;
                }
            }
            if(this.slotHostMap==null||this.slotHostMap.size()==0){
                LOG.info("checkAndInitClusterState 2 slotHostMap is null");
                return false;
            }
            //检查master节点数
            int masterCount=0;
            Map<String, JedisPool> map=jedisCluster.getClusterNodes();
            for (String key:map.keySet()) {
                try {//如果redis的某个节点挂掉了，会报错，需要捕捉异常才可以。但是master节点的数目不变
                    JedisPool jedisPool=map.get(key);
                    String info=jedisPool.getResource().info();
                    if (info.contains("role:master")) masterCount++;
                } catch (Exception e) {
                }
            }
            if (this.slotHostMap.size()!=masterCount*2) {
                LOG.info("checkAndInitClusterState 3 masterCount:"+masterCount+" slotHostMap size:"+slotHostMap.size()+" slotHostMap json:"+ JsonUtil.toJson(this.slotHostMap));
                return false;
            }
            //检查集群能否读写数据
            try {
                String state = jedisCluster.set("state", "1");
                if (!state.equals("OK")){
                    LOG.info("checkAndInitClusterState 4 state:"+state);
                    return false;
                }
            } catch (Exception e) {
                LOG.info("checkAndInitClusterState 5 e:"+e.getMessage());
                return false;
            }
            return true;
        } catch (Exception e) {
            LOG.info("checkAndInitClusterState 6 e:"+e.getMessage());
            return false;
        }
    }

    public static TreeMap<Long, String> getSlotHostMap(String node) {
        TreeMap<Long, String> hostMap = new TreeMap<Long, String>();
        try {
            String[] tmp = node.split(":");
            Jedis jedis = new Jedis(tmp[0], Integer.parseInt(tmp[1]));
            List<Object> slots = jedis.clusterSlots();
            for (Object obj : slots) {
                List<Object> list = (List<Object>) obj;
                List<Object> master = (List<Object>) list.get(2);
                String hostPort = new String((byte[]) master.get(0)) + ":" + master.get(1);
                hostMap.put((Long) list.get(0), hostPort);
                hostMap.put((Long) list.get(1), hostPort);
            }
            jedis.close();
        } catch (Exception e) {
            return null;
        }
        return hostMap;
    }
    /**
     * 文件名：RedisOperateType
     * 创建人：hongqiang.zhan
     * 创建日期：2016-6-1 下午2:45:52
     * 功能描述：Redis操作类型
     */
    public static enum RedisOperateType {
        SET_KV("key-value格式的数据put到redis中", 1),
        SET_HM("hash-map格式的数据put到redis中", 2),
        GET_KV("key-value格式的数据从redis读取", 3),
        GET_HM("hash-map格式的数据从redis读取", 4);
        // 成员变量
        private String name;
        private int value;

        // 构造方法
        private RedisOperateType(String name, int value) {
            this.name = name;
            this.value = value;
        }

        // get set 方法
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

    }
}