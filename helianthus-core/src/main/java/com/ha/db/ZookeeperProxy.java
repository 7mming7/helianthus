package com.ha.db;

import com.google.common.collect.Lists;
import com.ha.util.DateUtils;
import com.ha.util.RowUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

/**
 * User: shuiqing
 * DateTime: 17/4/5 下午6:16
 * Email: helianthus301@163.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class ZookeeperProxy {

    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperProxy.class);

    private static CuratorFramework zkClient;	//客户端对象
    /**
     * 功能：初始化Zookeeper操作对象
     * @param zkHosts	zookeeper的ip地址，以","分隔
     */
    public ZookeeperProxy(String zkHosts){
        if(zkClient == null){
            synchronized (ZookeeperProxy.class) {
                if(zkClient == null){
                    zkClient = CuratorFrameworkFactory.builder()
                            .connectString(zkHosts)
                            .sessionTimeoutMs(5000)
                            .connectionTimeoutMs(3000)
                            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                            .build();
                    zkClient.start();
                }
            }
        }
    }

    /**
     * 功能：在指定的临时节点上赋值
     * @param nodePath
     * @param nodeValue
     * @throws Exception
     */
    public void setEphemeralNodeValue(String nodePath, String nodeValue) throws Exception{
        if(zkClient.checkExists().forPath(nodePath) == null){
            zkClient.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(nodePath, nodeValue.getBytes());
        } else {
            zkClient.setData().forPath(nodePath, nodeValue.getBytes());
        }
    }

    /**
     * 功能：给指定的永久节点上赋值
     * @param nodePath
     * @param nodeValue
     * @throws Exception
     */
    public void setPersistNodeValue(String nodePath, String nodeValue) throws Exception{
        if(zkClient.checkExists().forPath(nodePath) == null){
            zkClient.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(nodePath, nodeValue.getBytes());
        } else {
            zkClient.setData().forPath(nodePath, nodeValue.getBytes());
        }
    }

    /**
     * 功能：监听指定节点
     * @param nodePath
     * @param listener
     * @throws Exception
     */
    public void listenNode(String nodePath, NodeCacheListener listener) throws Exception{
        NodeCache nodeCache = new NodeCache(zkClient, nodePath);
        nodeCache.start(true);
        nodeCache.getListenable().addListener(listener);
    }

    /**
     * 功能：获取子路径集合
     * @param parentPath
     * @return
     */
    public List<String> getChildNode(String parentPath){
        List<String> childPathList = Lists.newArrayList();
        try {
            Iterator<String> pathIte = zkClient.getChildren().forPath(parentPath).iterator();
            while(pathIte.hasNext()){
                childPathList.add(pathIte.next());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return childPathList;
    }

    /**
     * 功能：获取指定节点的值
     * @param nodePath
     * @return
     * @throws Exception
     */
    public String getDataValue(String nodePath){
        try {
            return new String(zkClient.getData().forPath(nodePath));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void close(){
        if(zkClient != null) CloseableUtils.closeQuietly(zkClient);
    }

    private static String[] taskIds = new String[] {
            "0" , "1" , "2" , "3" , "4" , "5" ,
            "6" , "7" , "8" , "9" , "a" , "b" ,
            "c" , "d" , "e" , "f" , "g" , "h" ,
            "i" , "j" , "k" , "l" , "m" , "n" ,
            "o" , "p" , "q" , "r" , "s" , "t" ,
            "u" , "v" , "w" , "x" , "y" , "z" ,
            "A" , "B" , "C" , "D" , "E" , "F" ,
            "G" , "H" , "I" , "J" , "K" , "L" ,
            "M" , "N" , "O" , "P" , "Q" , "R" ,
            "S" , "T" , "U" , "V" , "W" , "X" ,
            "Y" , "Z" , "#" , "@"
    };

    private synchronized int getTaskIdIndex(int type) throws Exception {
        int index = -1;

        String tempPath = "/distribute_lock";
        try {
            if(zkClient.checkExists().forPath(tempPath) == null){
                zkClient.create().withMode(CreateMode.PERSISTENT).forPath(tempPath);
            }
        } catch (Exception e) {
            // TODO: handle exception
            LOG.error(e.getMessage());
        }
        if (type==0) {
            tempPath=tempPath+"/storm";
        }else {
            tempPath=tempPath+"/hadoop";
        }
        try {
            zkClient.create().withMode(CreateMode.PERSISTENT).forPath(tempPath);
        } catch (Exception e) {
            return index;
        }

        try {
            String nodePath = "/task_id_index";
            if(zkClient.checkExists().forPath(nodePath) == null){
                zkClient.create().withMode(CreateMode.PERSISTENT).forPath(nodePath);
            }
            if (type==0) {
                nodePath = nodePath+"/storm";
            }else {
                nodePath = nodePath+"/hadoop";
            }
            String today = DateUtils.getToday();
            String date = null;
            if(zkClient.checkExists().forPath(nodePath) == null){
                zkClient.create().withMode(CreateMode.PERSISTENT).forPath(nodePath);
            } else {
                String value = new String(zkClient.getData().forPath(nodePath));
                if (value.split(RowUtil.ROW_SPLIT).length==2) {
                    date = RowUtil.getRowField(value, 0);
                    index = RowUtil.getRowIntField(value, 1);
                }else if (value.split(RowUtil.ROW_SPLIT).length==1) {
                    date = null;
                    index = RowUtil.getRowIntField(value, 0);
                }
            }
            //如果上一次调用方法不是今天，直接index=0
            if(date!=null&&!date.equals(today)){
                index=0;
            }else {
                index++;
            }
            if (index>=taskIds.length*taskIds.length) {
                index = 0;
            }
            String valueString = today+RowUtil.ROW_SPLIT+index;
            zkClient.setData().forPath(nodePath, valueString.getBytes());
        } finally {
            zkClient.delete().forPath(tempPath);
        }

        return index;
    }

    public String getTaskId(int type) throws Exception {
        int count = 0;
        int length = taskIds.length;
        while(true) {
            int index = getTaskIdIndex(type);
            if (index != -1) {
                if(index>=length){
                    int m = index/length;
                    int n = index%length;
                    return taskIds[m]+taskIds[n];
                }else {
                    return taskIds[index];
                }
            }
            count++;
            String typeString = type==0?"Strom":"Hadoop";
            LOG.info(typeString+" getTaskId count:"+count);
            Thread.sleep(100);
        }
    }

    /**
     * 功能：删除hadoop的分布式锁
     */
    public static synchronized void deleteHadoopLock(){
        String tempPath = "/distribute_lock/hadoop";
        try {
            if(zkClient.checkExists().forPath(tempPath) != null){
                zkClient.delete().forPath(tempPath);
                LOG.info("delete /distribute_lock/hadoop success!");
            }else {
                LOG.info("/distribute_lock/hadoop is not exists!");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 函数名：delPath
     * 功能描述：删除目录
     * @param path
     * @throws Exception
     */
    private static void delPath(CuratorFramework client,String path) throws Exception{
        List<String> paths=client.getChildren().forPath(path);
        for (String p:paths){
            delPath(client,path+"/"+p);
        }
        for(String p:paths){
            client.delete().forPath(path+"/"+p);
        }
    }

    public static void main(String[] args) throws Exception {
        final ZookeeperProxy zkProxy = new ZookeeperProxy("10.100.2.92,10.100.2.93,10.100.2.94");
//		ExecutorService executor = Executors.newFixedThreadPool(100);
//		for (int i = 0; i < 36; i++) {
//			executor.execute(new Runnable() {
//				@Override
//				public void run() {
//					try {
//						System.out.println("==================" + zkProxy.getTaskId(0) + "==================");
//						deleteStormLock();
//					} catch (Exception e) {e.printStackTrace();}
//				}
//			});
//		}
//		Thread.sleep(2000);
//		executor.shutdown();
        zkProxy.close();
    }
}
