package com.ha.exception;

import java.io.IOException;

/**
 * 表配置不存在异常
 * User: shuiqing
 * DateTime: 17/4/7 下午3:55
 * Email: helianthus301@163.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class TableConfNotFoundException extends IOException {

    private static final long serialVersionUID = 1L;

    public TableConfNotFoundException(String tableName){
        super("The table " + tableName + " config not found!");
    }

}