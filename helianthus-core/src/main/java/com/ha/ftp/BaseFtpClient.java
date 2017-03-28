package com.ha.ftp;

import java.io.InputStream;

/**
 * FTP接口类
 * User: shuiqing
 * DateTime: 16/11/18 下午4:51
 * Email: helianthus301@163.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface BaseFtpClient {

    /**
     * 上传文件
     * @param url
     *            FTP服务器hostname
     * @param port
     *            FTP服务器端口
     * @param username
     *            FTP登录账号
     * @param password
     *            FTP登录密码
     * @param path
     *            FTP服务器保存目录,如果是根目录则为“/”
     * @param filename
     *            上传到FTP服务器上的文件名
     * @param input
     *            本地文件输入流
     * @return 上传是否成功
     */
    public boolean uploadFileOnce(String url, int port,
                                     String username, String password,
                                     String path, String filename,
                                     InputStream input);

    /**
     * 从FTP服务器下载文件
     *
     * @param url
     *            FTP服务器hostname
     * @param port
     *            FTP服务器端口
     * @param username
     *            FTP登录账号
     * @param password
     *            FTP登录密码
     * @param remotePath
     *            FTP服务器上的相对路径
     * @param fileName
     *            要下载的文件名
     * @param localPath
     *            下载后保存到本地的路径
     * @return
     */
    public boolean downFileOnce(String url, int port,
                            String username, String password,
                            String remotePath, String fileName,
                            String localPath);
}
