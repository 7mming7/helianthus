package com.ha.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * hbase rowkey 工具类
 * User: shuiqing
 * DateTime: 17/4/5 下午6:29
 * Email: helianthus301@163.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class RowUtil {
    private static Logger logger = LoggerFactory.getLogger(RowUtil.class);
    /**
     * hbase中rowkey行分隔符
     */
    public static final String ROW_SPLIT = StringUtil.STRING_17;//hbase中rowkey行分隔符
    /**
     * hbase中列名分隔符
     */
    public static final String COL_SPLIT = "-";					//hbase中列名分隔符

    public static final String FIRST_CHAR = StringUtil.STRING_FIRST;	//最小字符
    public static final String END_CHAR = StringUtil.STRING_END;		//最大字符

    /**
     * 功能：根据rowkey获取指定索引号的字段
     * @param row	rowkey值
     * @param index	索引号
     * @return	字段值
     */
    public static String getRowField(String row, int index){
        return getRowField(row, index, ROW_SPLIT);
    }

    /**
     * 功能：根据rowkey获取指定索引号的字段，并转为Integer类型
     * @param row	rowkey值
     * @param index	索引号
     * @return	字段值
     */
    public static Integer getRowIntField(String row, int index){
        String[] data = StringUtil.splitStr(row, RowUtil.ROW_SPLIT);

        if(data.length <= index)
            return null;
        try{
            return Integer.parseInt(data[index]);
        } catch(Exception e){
            logger.error("error to getRowIntField, data:" + data[index], e);
        }
        return null;
    }

    /**
     * 功能：根据rowkey和分隔符获取指定索引号的字段，并转为Integer类型
     * @param row		rowkey值
     * @param index		索引号
     * @param split		分隔符
     * @return	字段值
     */
    public static String getRowField(String row, int index, String split){
        String[] data = StringUtil.splitStr(row, split);
        if(data.length <= index || data[index].trim().length() == 0)
            return null;
        return data[index];
    }

    public static Integer getRowIntField(String row, int index, String split){
        String data = getRowField(row, index, split);
        if(data == null) return null;
        return Integer.parseInt(data);
    }

    /**
     * 功能：给rowkey中的字段赋值
     * @param rowkey
     * @param index
     * @param value
     * @return
     */
    public static String setRowkeyValue(String rowkey, Integer index, Object value) {
        String[] tmp = StringUtil.splitStr(rowkey, RowUtil.ROW_SPLIT);
        tmp[index] = String.valueOf(value);

        return StringUtil.joinString(RowUtil.ROW_SPLIT, tmp);
    }

    /**
     * 功能：删除rowkey中的字段
     * @param rowkey
     * @param index
     * @return
     */
    public static String removeRowkeyValue(String rowkey, Integer index) {
        String[] tmp = StringUtil.splitStr(rowkey, RowUtil.ROW_SPLIT);
        tmp[index] = "";
        return StringUtil.joinString(RowUtil.ROW_SPLIT, tmp);
    }


    public static void main(String[] args) {
        System.out.println(String.valueOf(16));
        char ch = 16;
        System.out.println(String.valueOf(ch));
    }

}