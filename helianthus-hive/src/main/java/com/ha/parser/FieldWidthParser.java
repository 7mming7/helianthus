package com.ha.parser;

import com.ha.base.BaseField;

/**
 * 字段宽度解析
 * User: shuiqing
 * DateTime: 17/4/7 下午3:59
 * Email: helianthus301@163.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface FieldWidthParser {

    /**
     * 是否支持解析该字段的长度
     * @param bf
     * @return
     */
    boolean supportWidth(BaseField bf);

    /**
     * 解析指定字段的宽度
     * @param bf
     * @return
     */
    int parseWidth(BaseField bf) throws UnsupportedOperationException;

    /**
     * 是否支持此字段的类型
     * @param bf
     * @return
     */
    boolean supportType(BaseField bf);

    /**
     * 解析此字段的类型
     * @param bf
     * @return
     */
    String parseType(BaseField bf) throws UnsupportedOperationException;
}