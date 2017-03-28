package com.ha.ftp;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.*;

/**
 * SFTP实现文件的上传下载
 * User: shuiqing
 * DateTime: 16/11/18 下午5:05
 * Email: helianthus301@163.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class SFTPClient implements BaseFtpClient {


    @Override
    public boolean uploadFileOnce(String url, int port,
                              String username, String password,
                              String path, String filename, InputStream input) {
        Session session = null;
        Channel channel = null;
        try {
            JSch jsch = new JSch();

            if(port <=0){
                //连接服务器，采用默认端口
                session = jsch.getSession(username, url);
            }else{
                //采用指定的端口连接服务器
                session = jsch.getSession(username, url ,port);
            }

            //如果服务器连接不上，则抛出异常
            if (session == null) {
                throw new Exception("session is null");
            }

            //设置登陆主机的密码
            session.setPassword(password);//设置密码
            //设置第一次登陆的时候提示，可选值：(ask | yes | no)
            session.setConfig("StrictHostKeyChecking", "no");
            //设置登陆超时时间
            session.connect(30000);

            //创建sftp通信通道
            channel = (Channel) session.openChannel("sftp");
            channel.connect(1000);
            ChannelSftp sftp = (ChannelSftp) channel;


            //进入服务器指定的文件夹
            sftp.cd(path);
            sftp.put(input,filename);
            input.close();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            session.disconnect();
            channel.disconnect();
            return true;
        }
    }

    @Override
    public boolean downFileOnce(String url, int port,
                            String username, String password,
                            String remotePath, String fileName, String localPath) {
        Session session = null;
        Channel channel = null;
        try {
            JSch jsch = new JSch();

            if(port <=0){
                //连接服务器，采用默认端口
                session = jsch.getSession(username, url);
            }else{
                //采用指定的端口连接服务器
                session = jsch.getSession(username, url ,port);
            }

            //如果服务器连接不上，则抛出异常
            if (session == null) {
                throw new Exception("session is null");
            }

            //设置登陆主机的密码
            session.setPassword(password);//设置密码
            //设置第一次登陆的时候提示，可选值：(ask | yes | no)
            session.setConfig("StrictHostKeyChecking", "no");
            //设置登陆超时时间
            session.connect(30000);

            //创建sftp通信通道
            channel = (Channel) session.openChannel("sftp");
            channel.connect(1000);
            ChannelSftp sftp = (ChannelSftp) channel;

            //进入服务器指定的文件夹
            sftp.cd(remotePath);
            sftp.get(fileName,localPath);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            session.disconnect();
            channel.disconnect();
            return true;
        }
    }

    public static void main(String[] args){
        SFTPClient sftpClient = new SFTPClient();
        /*try {
            FileInputStream inputStream = new FileInputStream(new File("/Users/gemingming/Desktop/IDA-ETL-流程图.graffle"));
            sftpClient.uploadFileOnce("192.168.1.61",22,"root","700103","/home/master",
                    "IDA-ETL-流程图.graffle", inputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
        sftpClient.downFileOnce("192.168.1.61",22,"root","700103","/home/master",
                "123.txt", "/Users/gemingming/Desktop");
    }
}
