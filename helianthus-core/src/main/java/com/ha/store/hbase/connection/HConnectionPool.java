package com.ha.store.hbase.connection;

import com.ha.config.HelianthusConfig;
import org.apache.hadoop.hbase.client.HConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * hbase connection pool.
 * User: shuiqing
 * DateTime: 17/8/23 下午5:18
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class HConnectionPool {

    private final static Logger LOG = LoggerFactory.getLogger(HConnectionPool.class);

    private static List<HConnection> connections = new ArrayList<HConnection>();	//容器，空闲连接
    private static List<ExecutorService> executorList = new ArrayList<ExecutorService>();
    private static AtomicInteger num = new AtomicInteger(0);
    private static int hbaseHConnectionNum;
    private static String zookeeper;
    private static int poolSize;

    /**
     * 功能：初始化hbase连接池
     * @param helianthusConfig
     */
    public static void initHConnectionPool(HelianthusConfig helianthusConfig) {
        initHConnectionPool(HelianthusConfig.getHbaseHConnectionNum(), HelianthusConfig.getZookeeper(), HelianthusConfig.getHbasePoolSize());
    }

    /**
     * 功能：初始化连接池
     * @param hbaseHConnectionNum	需要创建的HConnection个数
     * @param zookeeper			    zookeeper地址
     * @param poolSize				每个HConnection并发数
     */
    public static void initHConnectionPool(int hbaseHConnectionNum, String zookeeper, int poolSize) {
        if(connections == null || connections.size()  == 0){
            synchronized (HConnectionPool.class) {
                if(connections == null || connections.size()  == 0){
                    for(int num = 0; num < hbaseHConnectionNum; num++){
                        ExecutorService pool = Executors.newFixedThreadPool(poolSize);
                        executorList.add(pool);
                        connections.add(HbaseUtils.createHConnection(zookeeper, pool));
                    }
                    HConnectionPool.hbaseHConnectionNum = hbaseHConnectionNum;
                    HConnectionPool.zookeeper = zookeeper;
                    HConnectionPool.poolSize = poolSize;
                }
            }
        }
    }

    /**
     * 从连接池里得到连接
     * @return
     */
    public static HConnection getConnection(){
        if(connections.size() == 0)
            initHConnectionPool(hbaseHConnectionNum, zookeeper, poolSize);
        int index = num.incrementAndGet() % hbaseHConnectionNum;
        return connections.get(index);
    }

    /**
     * 功能：关闭连接池
     */
    public static synchronized void close(){
        for(int i = 0; i < connections.size(); i++){
            try {
                connections.get(i).close();
            } catch (IOException e) {
                LOG.error("error to close hbase connection");
            }
        }
        for(int i = 0; i < executorList.size(); i++){
            executorList.get(i).shutdown();
        }
        connections.clear();
        executorList.clear();
    }
}
