package com.ha.file;

import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * 查找字段定义
 * User: shuiqing
 * DateTime: 17/4/7 下午3:46
 * Email: helianthus301@163.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface FieldDefineFind {

    /**
     * 根据文件名称查找对应的
     * @param fileName
     * @return
     */
    LinkedHashMap<String, Integer> findByFileName(String fileName)throws IOException;

    /**
     * 根据表名查询
     * @param fileName
     * @return
     * @throws IOException
     */
    LinkedHashMap<String, Integer> findByTableName(String fileName) throws IOException;
}