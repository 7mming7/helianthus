package com.ha.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * User: shuiqing
 * DateTime: 17/4/5 下午6:24
 * Email: helianthus301@163.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class StringUtil {

    //分隔符
    public static final String ARUGEMENT_SPLIT = "-";
    public static final String PATH_SPLIT = "/";

    //hbase rowkey分隔符
    private static final char character_first = Character.MIN_VALUE;
    private static final char character_end = Character.MAX_VALUE;
    public static final String STRING_FIRST = String.valueOf(character_first);	//最小字符
    public static final String STRING_END = String.valueOf(character_end);		//最大字符

    //用于hbase rowkey 分割符
    private static final char  ch_17 = 17;
    public static final String STRING_17 = String.valueOf(ch_17); // char(16）用于分隔符， vim => ctrl + p

    //MapReduce中字段分隔符
    public static final  char    ch_21 = 21;
    public static final  String  STRING_21 = String.valueOf(ch_21);        //分隔符

    //日志中字段分隔符
    private static final char  ch_26 = 26;
    public static final String STRING_26 = String.valueOf(ch_26); 		// char(26）用于分隔符， vim => ctrl + z

    public static final  char    ch_22 = 22;
    public static final  String  STRING_22 = String.valueOf(ch_22);        //分隔符

    /**
     * 填充后缀字符串，使其达到{@code size}大小
     * @param data
     * @param size
     * @param filledChar
     * @return
     */
    public static String fillRightData(String data, int size, char filledChar) {
        String result = String.format("%1$-" + size + "s", data).replace(' ', filledChar);
        return result;
    }

    /**
     * 填充前缀字符串，使其达到{@code size}大小
     * @param data
     * @param size
     * @param filledChar
     * @return
     */
    public static String fillLeftData(String data, int size, char filledChar) {
        String result = String.format("%1$" + size + "s", data).replace(' ', filledChar);
        return result;
    }

    public static String fillLeftData(int num) {
        return String.format("%02d", num);
    }

    /**
     * String is null or empty string.
     * */
    public static boolean isEmptyString(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * String is NOT null or empty string.
     * */
    public static boolean isNotEmptyString(String str) {
        return !isEmptyString(str);
    }

    /**
     * 功能：根据数值产生定长字符串
     * @param num 数值
     * @return 定长字符串
     */
    public static String getIntegerString(Integer num) {
        return StringUtil.fillLeftData(String.valueOf(num), String.valueOf(Integer.MAX_VALUE).length(), '0');
    }

    /**
     * 功能：根据数值产生定长字符串
     * @param num 数值
     * @return 定长字符串
     */
    public static String getLongString(Long num) {
        return StringUtil.fillLeftData(String.valueOf(num), String.valueOf(Long.MAX_VALUE).length(), '0');
    }

    /**
     * 功能：根据数值产生定长字符串
     * @param num 数值
     * @return 定长字符串
     */
    public static String getConverseIntegerString(Integer num) {
        int tmp = Integer.MAX_VALUE - num;
        return StringUtil.fillLeftData(String.valueOf(tmp), String.valueOf(Integer.MAX_VALUE).length(), '0');
    }

    /**
     * 功能：根据数值产生定长字符串
     * @param num 数值
     * @return 定长字符串
     */
    public static String getConverseLongString(Long num) {
        long tmp = Long.MAX_VALUE - num;
        return StringUtil.fillLeftData(String.valueOf(tmp), String.valueOf(Long.MAX_VALUE).length(), '0');
    }

    /**
     * 功能：字符串拼接
     * @param split
     * @param values
     * @return
     */
    public static String joinString(String split, Object... values) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < values.length; i++) {
            if (i < values.length - 1) {
                buffer.append(values[i]).append(split);
            } else {
                buffer.append(values[i]);
            }
        }
        return buffer.toString();
    }

    /**
     * 功能：转int
     * @param value
     * @return
     */
    public static Integer convertStringToInt(String value){
        if(value == null || value.trim().length() == 0) return null;
        return Integer.parseInt(value);
    }

    public static String convertStringToString(String value){
        if(value == null || value.trim().length() == 0) return null;
        return value;
    }

    /**
     * 功能：转long
     * @param value
     * @return
     */
    public static Long convertStringToLong(String value){
        if(value == null || value.trim().length() == 0) return null;
        return Long.parseLong(value);
    }

    /**
     * 功能：转boolean
     * @param value
     * @return
     */
    public static Boolean convertStringToBoolean(String value){
        if(value == null || value.trim().length() == 0) return null;
        return Boolean.parseBoolean(value);
    }

    /**
     * 功能：根据分隔符进行分割
     * @param str
     * @param split
     * @return
     */
    public static String[] splitStr(String str, String split){
        return str.split(split, str.length());
    }

    public static boolean isALLNumeric(String str){
        if(str == null || str.length() == 0) return false;

        for(int i = 0; i < str.length(); i++){
            if(!(str.charAt(i) >= '0' && str.charAt(i) <= '9')){
                return false;
            }
        }
        return true;
    }

    /**
     * 功能：全角转半角， ？=> ?
     * @param input
     * @return
     */
    public static String replaceDBC2SBC(String input) {
        Pattern pattern = Pattern.compile("[\u3000\uff01-\uff5f]{1}");

        Matcher m = pattern.matcher(input);
        StringBuffer s = new StringBuffer();
        while (m.find()) {
            char c = m.group(0).charAt(0);
            char replacedChar = c == '　' ? ' ' : (char) (c - 0xfee0);
            m.appendReplacement(s, String.valueOf(replacedChar));
        }

        m.appendTail(s);

        return s.toString();
    }
}
