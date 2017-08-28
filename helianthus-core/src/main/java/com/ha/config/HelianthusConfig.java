package com.ha.config;

import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * helianthus基础配置对象
 * User: shuiqing
 * DateTime: 17/3/27 上午11:09
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Getter
@Setter
public class HelianthusConfig implements Serializable {

    private static final long serialVersionUID = 6211344970724898565L;

    public final static int SESSION_TIMEOUT_TIME = 30 * 60;  //秒级别,会话超时时间，默认30分钟

    /**
     * redis集群
     */
    private static List<String> redisCluster;

    /**
     * hdfs地址
     */
    private static String hdfsClusterName;		//hdfs的集群名
    private static String hdfsAddr;			    //hdfs namenode, secondary namenode地址，ip:port,ip:port

    /**
     * hbase地址
     */
    private static String zookeeper;			//zookeeper地址
    private static int hbasePoolSize;			//hbase线程池大小
    private static int hbaseHConnectionNum;	    //hbase hconnection数量

    /**
     * api端口
     */
    private static int apiThriftPort;			//api thrift端口

    /**
     * mapreduce配置
     */
    private static long  mrInputSplitSize;      //mapreduce中map输入分片的最大大小(单位：byte)
    private static int   mrMaxMapNum;           //mapreduce中map的最大个数
    private static float mrMapReduceNumRate;    //mapreduce中map个数与reduce个数的比值

    private static int maxKpiMapSize;           //缓存数量
    private static int maxDistinctMapSize;      //Bitmap,基数估算，hashset数据缓存数量
    private static int maxDistinctFieldSize;    //Hashset最大数据量阈值

    private static long storeBoltFlushExecutorPeriod;		//KpiStore top flush定时器时间间隔,单位毫秒
    private static long cacheBoltRedisBatchSize;			//cacheBolt中与redis交互的批量大小

    /**
     * hive配置
     */
    private static String hiveDatabaseName;     //hive数据库名称
    private static String hiveDatabaseDir;      //hive数据库存储目录
    private static String hiveDatabaseTmpDir;   //hive临时库存储目录
    private static String hiveSourceDir;        //源数据存放目录
    private static String hiveHdfsDir;          //转化后压缩文件的HDFS存储目录
    private static String hiveTableLocation;    //hive表存储位置
    private static String hiveTableTerminated;  //hive表分隔符

    /**
     * 初始化配置
     * @param filePath 配置文件路径
     */
    private HelianthusConfig(String filePath) throws IOException {
        RichProperties props = new RichProperties();
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            props.load(is);
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) { }
        }

        //redis
        redisCluster = props.getList("redis.cluster");

        apiThriftPort = props.getInt("web.stats.service.thrift.port", 55555);

        //hdfs
        hdfsClusterName = props.getString("hdfs.cluster.name");
        hdfsAddr = props.getString("hdfs.address");

        //hbase
        zookeeper = props.getString("hbase.zookeeper.quorum");
        hbasePoolSize = props.getInt("hbase.connect.poolSize", 50);
        hbaseHConnectionNum = props.getInt("hbase.connect.num", 20);

        //mapreduce
        maxKpiMapSize=props.getInt("mapreduce.cacheMapper.maxKpiMapSize", 50000);//默认50000
        maxDistinctMapSize=props.getInt("mapreduce.cacheMapper.maxDistinctMapSize", 5000);//默认5000
        maxDistinctFieldSize=props.getInt("mapreduce.cacheMapper.maxDistinctFieldSize",1000000);//默认值100万

        mrInputSplitSize = Long.parseLong(props.getString("mapreduce.input.split.size", "2000000000"));     //默认2G
        mrMaxMapNum = props.getInt("mapreduce.map.tasks.maxnum", 8);	               						 //默认8个map
        mrMapReduceNumRate = Float.parseFloat(props.getString("mapreduce.mapandreduce.tasks.rate", "0.3")); //默认0.3

        //hive
        hiveDatabaseName = props.getString("hive.database.name");
        hiveDatabaseDir = props.getString("hive.database.dir");
        hiveDatabaseTmpDir = props.getString("hive.database.tmpDir");
        hiveSourceDir = props.getString("hive.source.dir");
        hiveHdfsDir = props.getString("hive.hdfs.dir");
        hiveTableLocation = props.getString("hive.table.location");
        hiveTableTerminated = props.getString("hive.table.terminated");
    }

    /**
     * 加载初始化配置
     * @return config配置对象
     * @throws IOException
     */
    public static HelianthusConfig loadConfig() throws IOException{
        //读取执行程序所在目录，获取配置文件路径
        File directory	= new File(".");
        String thisDirPath = directory.getCanonicalPath();
        if(thisDirPath.endsWith("helianthus")){
            return new HelianthusConfig(thisDirPath + "/helianthus-init/src/main/resources/helianthus.properties");
        } else {
            return new HelianthusConfig("../helianthus-init/src/main/resources/helianthus.properties");
        }
    }

    /**
     * 根据自定义的类型，
     */
    public static class RichProperties extends Properties {
        private static final long serialVersionUID = 1L;

        public Integer getInt(String name){
            return getInt(name, null);
        }

        public Integer getInt(String name, Integer defaultValue){
            String value = super.getProperty(name);
            if(value == null){
                return defaultValue;
            } else {
                return Integer.parseInt(value.trim());
            }
        }

        public Double getDouble(String name){
            return getDouble(name, null);
        }

        public Double getDouble(String name, Double defaultValue){
            String value = super.getProperty(name);
            if(value == null){
                return defaultValue;
            } else {
                return Double.  parseDouble(value.trim());
            }
        }

        public String getString(String name){
            return getString(name, null);
        }

        public String getString(String name, String defaultValue){
            String value = super.getProperty(name);
            if(value == null){
                return defaultValue;
            } else {
                return value.trim();
            }
        }

        /**
         * 功能：获取集合，各个属性用“，”分割
         * @param name
         * @return
         */
        public List<String> getList(String name){
            List<String> list = new ArrayList<String>();
            String value = super.getProperty(name);
            if(value != null) {
                for(String str : value.split(",")) {
                    list.add(str.trim());
                }
            }
            return list;
        }

        public boolean getBoolean(String name, boolean defaultValue){
            String value = super.getProperty(name);
            if(value != null){
                return Boolean.parseBoolean(value);
            }
            return defaultValue;
        }
    }

    public static List<String> getRedisCluster() {
        return redisCluster;
    }

    public static void setRedisCluster(List<String> redisCluster) {
        HelianthusConfig.redisCluster = redisCluster;
    }

    public static String getHdfsClusterName() {
        return hdfsClusterName;
    }

    public static void setHdfsClusterName(String hdfsClusterName) {
        HelianthusConfig.hdfsClusterName = hdfsClusterName;
    }

    public static String getHdfsAddr() {
        return hdfsAddr;
    }

    public static void setHdfsAddr(String hdfsAddr) {
        HelianthusConfig.hdfsAddr = hdfsAddr;
    }

    public static String getZookeeper() {
        return zookeeper;
    }

    public static void setZookeeper(String zookeeper) {
        HelianthusConfig.zookeeper = zookeeper;
    }

    public static int getHbasePoolSize() {
        return hbasePoolSize;
    }

    public static void setHbasePoolSize(int hbasePoolSize) {
        HelianthusConfig.hbasePoolSize = hbasePoolSize;
    }

    public static int getHbaseHConnectionNum() {
        return hbaseHConnectionNum;
    }

    public static void setHbaseHConnectionNum(int hbaseHConnectionNum) {
        HelianthusConfig.hbaseHConnectionNum = hbaseHConnectionNum;
    }

    public static int getApiThriftPort() {
        return apiThriftPort;
    }

    public static void setApiThriftPort(int apiThriftPort) {
        HelianthusConfig.apiThriftPort = apiThriftPort;
    }

    public static long getMrInputSplitSize() {
        return mrInputSplitSize;
    }

    public static void setMrInputSplitSize(long mrInputSplitSize) {
        HelianthusConfig.mrInputSplitSize = mrInputSplitSize;
    }

    public static int getMrMaxMapNum() {
        return mrMaxMapNum;
    }

    public static void setMrMaxMapNum(int mrMaxMapNum) {
        HelianthusConfig.mrMaxMapNum = mrMaxMapNum;
    }

    public static float getMrMapReduceNumRate() {
        return mrMapReduceNumRate;
    }

    public static void setMrMapReduceNumRate(float mrMapReduceNumRate) {
        HelianthusConfig.mrMapReduceNumRate = mrMapReduceNumRate;
    }

    public static int getMaxKpiMapSize() {
        return maxKpiMapSize;
    }

    public static void setMaxKpiMapSize(int maxKpiMapSize) {
        HelianthusConfig.maxKpiMapSize = maxKpiMapSize;
    }

    public static int getMaxDistinctMapSize() {
        return maxDistinctMapSize;
    }

    public static void setMaxDistinctMapSize(int maxDistinctMapSize) {
        HelianthusConfig.maxDistinctMapSize = maxDistinctMapSize;
    }

    public static int getMaxDistinctFieldSize() {
        return maxDistinctFieldSize;
    }

    public static void setMaxDistinctFieldSize(int maxDistinctFieldSize) {
        HelianthusConfig.maxDistinctFieldSize = maxDistinctFieldSize;
    }

    public static long getStoreBoltFlushExecutorPeriod() {
        return storeBoltFlushExecutorPeriod;
    }

    public static void setStoreBoltFlushExecutorPeriod(long storeBoltFlushExecutorPeriod) {
        HelianthusConfig.storeBoltFlushExecutorPeriod = storeBoltFlushExecutorPeriod;
    }

    public static long getCacheBoltRedisBatchSize() {
        return cacheBoltRedisBatchSize;
    }

    public static void setCacheBoltRedisBatchSize(long cacheBoltRedisBatchSize) {
        HelianthusConfig.cacheBoltRedisBatchSize = cacheBoltRedisBatchSize;
    }

    public static String getHiveDatabaseName() {
        return hiveDatabaseName;
    }

    public static void setHiveDatabaseName(String hiveDatabaseName) {
        HelianthusConfig.hiveDatabaseName = hiveDatabaseName;
    }

    public static String getHiveDatabaseDir() {
        return hiveDatabaseDir;
    }

    public static void setHiveDatabaseDir(String hiveDatabaseDir) {
        HelianthusConfig.hiveDatabaseDir = hiveDatabaseDir;
    }

    public static String getHiveDatabaseTmpDir() {
        return hiveDatabaseTmpDir;
    }

    public static void setHiveDatabaseTmpDir(String hiveDatabaseTmpDir) {
        HelianthusConfig.hiveDatabaseTmpDir = hiveDatabaseTmpDir;
    }

    public static String getHiveSourceDir() {
        return hiveSourceDir;
    }

    public static void setHiveSourceDir(String hiveSourceDir) {
        HelianthusConfig.hiveSourceDir = hiveSourceDir;
    }

    public static String getHiveHdfsDir() {
        return hiveHdfsDir;
    }

    public static void setHiveHdfsDir(String hiveHdfsDir) {
        HelianthusConfig.hiveHdfsDir = hiveHdfsDir;
    }

    public static String getHiveTableLocation() {
        return hiveTableLocation;
    }

    public static void setHiveTableLocation(String hiveTableLocation) {
        HelianthusConfig.hiveTableLocation = hiveTableLocation;
    }

    public static String getHiveTableTerminated() {
        return hiveTableTerminated;
    }

    public static void setHiveTableTerminated(String hiveTableTerminated) {
        HelianthusConfig.hiveTableTerminated = hiveTableTerminated;
    }
}
