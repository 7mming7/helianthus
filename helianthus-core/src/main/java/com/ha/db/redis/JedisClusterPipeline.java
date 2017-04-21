package com.ha.db.redis;

import com.google.common.collect.Maps;
import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.PipelineBase;
import redis.clients.jedis.exceptions.JedisRedirectionException;
import redis.clients.util.JedisClusterCRC16;
import redis.clients.util.SafeEncoder;

import java.io.Closeable;
import java.util.*;

/**
 * redis pipeline 操作方式
 * User: shuiqing
 * DateTime: 17/4/13 下午4:58
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class JedisClusterPipeline extends PipelineBase implements Closeable {
    private TreeMap<Long, String> slotHostMap;
    private Map<String, JedisPool> nodeMap;
    private Map<String, Jedis> jedisMap = Maps.newHashMap();
    private Queue<Client> clients = new LinkedList<Client>();   // 根据顺序存储每个命令对应的Client

    public JedisClusterPipeline(Map<String, JedisPool> nodeMap, TreeMap<Long, String> slotHostMap){
        this.slotHostMap = slotHostMap;
        this.nodeMap = nodeMap;
    }

    /**
     * 同步读取所有数据. 与syncAndReturnAll()相比，sync()只是没有对数据做反序列化
     */
    public void sync() {
        try {
            for (Client client : clients) {
                generateResponse(client.getOne()).get();
            }
        } catch (JedisRedirectionException jre) {
            throw jre;
        } finally {
            close();
        }
    }

    @Override
    public void close() {
        clean();
        clients.clear();
        for (Jedis jedis : jedisMap.values()) {
            jedis.close();
        }
        jedisMap.clear();
    }

    @Override
    protected Client getClient(String key) {
        byte[] bKey = SafeEncoder.encode(key);
        return getClient(bKey);
    }

    @Override
    protected Client getClient(byte[] key) {
        long slot = JedisClusterCRC16.getSlot(key);
        String hostPort = slotHostMap.lowerEntry(slot + 1L).getValue();
        Jedis jedis = this.jedisMap.get(hostPort);
        if(jedis == null){
            jedis = nodeMap.get(hostPort).getResource();
            this.jedisMap.put(hostPort, jedis);
        }
        Client  client = jedis.getClient();
        clients.add(client);
        return client;
    }

    /**
     * redis 获取redis槽信息
     * @param node
     * @return
     */
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
}
