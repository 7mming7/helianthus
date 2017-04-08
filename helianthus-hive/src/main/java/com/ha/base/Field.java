package com.ha.base;

/**
 * 表字段
 * User: shuiqing
 * DateTime: 17/4/7 下午4:34
 * Email: helianthus301@163.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface Field extends Define{

    /**
     * 字段名称
     * @return
     */
    String getName();

    /**
     *
     * @param name
     */
    void setName(String name);

    /**
     * 返回字段类型
     * @return
     */
    String getType();

    /**
     *
     * @param type
     */
    void setType(String type);

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
}
