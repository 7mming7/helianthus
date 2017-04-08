package com.ha.base;

import java.util.List;

/**
 * 表接口
 * User: shuiqing
 * DateTime: 17/4/7 下午5:19
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface Table extends Define{

    /**
     * 字段分隔符
     * @return
     */
    String getTerminated();

    /**
     * 字段分隔符
     * @param terminated
     */
    void setTerminated(String terminated);

    /**
     * 得到描述
     * @return
     */
    String getComment();

    /**
     * 设置字段描述
     * @param comment
     */
    void setComment(String comment);

    /**
     * 设置表名
     * @param name
     */
    void setName(String name);

    /**
     * 得到表名
     * @return
     */
    String getName();

    /**
     * 是否为外部表
     * @return
     */
    boolean isExternal();

    /**
     * 设置是否为外部表
     * @param external
     */
    void setExternal(boolean external);

    /**
     * 添加字段
     * @param field
     */
    void addField(Field field);

    /**
     * 得到所有的字段
     * @return
     */
    List<Field> getFields();

    /**
     * 添加分区字段
     * @param field
     */
    void addPartitionField(Field field);

    /**
     * 得到分区字段
     * @return
     */
    List<Field> getPartitionFields();

    /**
     * 存储路径
     * @param location
     */
    void setLocation(String location);

    /**
     * 得到分区
     * @return
     */
    String getLocation();

    /**
     * 是否压缩
     * @param compressed
     */
    void setCompressed(boolean compressed);

    /**
     * 压缩类型
     */
    void setCompressedType(String type);
}