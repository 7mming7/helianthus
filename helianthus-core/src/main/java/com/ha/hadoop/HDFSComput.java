package com.ha.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * HDFS工具类
 * User: shuiqing
 * DateTime: 17/4/5 下午5:37
 * Email: helianthus301@163.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class HDFSComput {

    private static Logger log = LoggerFactory.getLogger(HDFSComput.class);

    private static String nameservices;	//hdfs集群名
    private static String hdfsAddr;		//namenode地址(ip:port,ip:port)

    /**
     * 功能：初始化hdfs操作类
     * @param nameservices	hdfs集群名、
     * @param hdfsAddr      hdfs地址
     */
    public HDFSComput(String nameservices, String hdfsAddr){
        this.nameservices = nameservices;
        this.hdfsAddr = hdfsAddr;
    }

    private static Configuration getConfiguration(){
        Configuration conf = new Configuration();
        //mycluster是hadoop集群名
        conf.set("fs.defaultFS", "hdfs://" + nameservices);
        conf.set("dfs.nameservices", nameservices);
        conf.set("dfs.ha.namenodes." + nameservices, "nn1,nn2");
        conf.set("dfs.namenode.rpc-address." + nameservices + ".nn1", hdfsAddr.split(",")[0].trim());
        conf.set("dfs.namenode.rpc-address." + nameservices + ".nn2", hdfsAddr.split(",")[1].trim());
        conf.set("dfs.client.failover.proxy.provider." + nameservices, "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        conf.set("dfs.replication", "2");
        return conf;
    }

    //获取hadoop的fs
    public synchronized static FileSystem getFileSystem() {
        FileSystem fs = null;
        try {
            Configuration conf = getConfiguration();
            fs = FileSystem.get(conf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fs;
    }

    /**
     * 功能：读取指定文件内容
     * @param path	hdfs中文件路径
     * @return
     */
    public byte[] readFileFromHdfs(String path){
        byte[] buffer = null;
        FSDataInputStream fsinput = null;
        FileSystem fs = null;
        try{
            Configuration conf = getConfiguration();
            fs = FileSystem.newInstance(conf);
            fsinput = fs.open(new Path(path));

            if (fsinput != null) {
                buffer = new byte[fsinput.available()];
                int off = 0,len = 0;
                int length = buffer.length;
                for(;;){
                    len = fsinput.read(buffer,off,length);
                    length -= len;
                    off += len;
                    if(off <= 0 || length <= 0)
                        break;
                }
            }
        }
        catch(Exception e){
            log.error("HdfsProxy:readFileFromHdfs error, path: " + path, e);
        } finally {
            IOUtils.closeStream(fsinput);
            IOUtils.closeStream(fs);
        }
        return buffer;
    }

    public void putData(String srcPath, String destPath){
        FileSystem fs = null;
        try{
            fs = FileSystem.get(getConfiguration());
            fs.copyFromLocalFile(false, true, new Path(srcPath), new Path(destPath));
        } catch(Exception e){
            log.error("error to put resource to  hdfs", e);
        } finally {
            try {
                if(fs != null)
                    fs.close();
            } catch (IOException e) {
                log.error("error to close hdfs FileSystem", e);
            }
        }
    }

    public boolean isFileExist(Path path){
        boolean result = false;
        FileSystem fs = null;
        try{
            fs = FileSystem.get(getConfiguration());
            result = fs.exists(path);
        } catch(Exception e){
            log.error("error to put resource to  hdfs", e);
        } finally {
            try {
                if(fs != null)
                    fs.close();
            } catch (IOException e) {
                log.error("error to close hdfs FileSystem", e);
            }
        }
        return result;
    }
}
