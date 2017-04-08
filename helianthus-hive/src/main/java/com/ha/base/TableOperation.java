package com.ha.base;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;

/**
 * hive 表创建接口
 * User: shuiqing
 * DateTime: 17/4/7 下午5:36
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface TableOperation {

    /**
     * 创建所有的表，返回创建结果信息
     * @return
     */
    TableInfo createAll(OutputStream out)throws IOException, URISyntaxException ;

    /**
     * 删除所有的表
     *  @return
     */
    TableInfo deleteAll(OutputStream out)throws IOException, URISyntaxException ;

    /**
     * 创建指定的表
     * @param table
     * @return
     */
    TableInfo create(OutputStream out,String table)throws IOException, URISyntaxException ;

    /**
     * 删除指定的表
     * @param table
     * @return
     */
    TableInfo delete(OutputStream out,String table)throws IOException, URISyntaxException ;

    /**
     * 重新创建表
     * @param table
     */
    TableInfo recreate(OutputStream out,String table)throws IOException, URISyntaxException ;

    /**
     * 重新创建所有的表
     * @return
     */
    TableInfo reCreateAll(OutputStream out)throws IOException, URISyntaxException;
}