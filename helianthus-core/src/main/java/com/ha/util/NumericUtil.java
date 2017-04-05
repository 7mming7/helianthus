package com.ha.util;

import java.math.BigInteger;

/**
 * 数字工具类
 * User: shuiqing
 * DateTime: 17/4/5 下午6:26
 * Email: helianthus301@163.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class NumericUtil {

    public static long getLongVal(Long value){
        return value == null?0l: value;
    }
    /**
     * 功能：两个long值相加
     * @param firstVal
     * @param secondVal
     * @return
     */
    public static Long addValue(Long firstVal, Long secondVal){
        if(firstVal == null && secondVal == null) return null;
        if(firstVal == null)  return secondVal;
        if(secondVal == null) return firstVal;
        return firstVal + secondVal;
    }

    /**
     * 功能：衰减指定值
     * @param halfLife		半衰期（天）
     * @param value			权重值
     * @param curTime		当前时间(毫秒)
     * @param originTime	上次时间(毫秒)
     * @return
     */
    public static double decayTagValue(int halfLife, double value, long curTime, long originTime){
        long dayInterval = (curTime - originTime) / (3600 * 24 * 1000); //计算相隔多少天
        double decayFactor =  Math.exp(- Math.log(2) * dayInterval / halfLife);

        return decayFactor * value;
    }

    /**
     * 功能：衰减指定值
     * @param halfLife		半衰期（天）
     * @param value			权重值
     * @param curDate		当前时间(YYYYMMDD)
     * @param originDate	上次时间(YYYYMMDD)
     * @return
     */
    public static double decayTagValue(int halfLife, double value, String curDate, String originDate){
        long curTime = DateUtils.getTimeByDay(curDate);
        long originTime = DateUtils.getTimeByDay(originDate);
        return decayTagValue(halfLife, value, curTime, originTime);
    }

    public static void main(String[] args) {
        String guid = "1459847924874805454";
        guid = "962b1862b97a3441e88bf68aaf390620";
        String compressGuid=CompressGuid(guid);
        System.out.println("compressGuid:"+compressGuid + " => " + compressGuid.getBytes().length);
        String uncompressGuid=UnCompressGuid(compressGuid);
        System.out.println("uncompressGuid:"+uncompressGuid);
    }
    //64进制字符
    final static char[] digits = {
            '0' , '1' , '2' , '3' , '4' , '5' ,
            '6' , '7' , '8' , '9' , 'a' , 'b' ,
            'c' , 'd' , 'e' , 'f' , 'g' , 'h' ,
            'i' , 'j' , 'k' , 'l' , 'm' , 'n' ,
            'o' , 'p' , 'q' , 'r' , 's' , 't' ,
            'u' , 'v' , 'w' , 'x' , 'y' , 'z' ,
            'A' , 'B' , 'C' , 'D' , 'E' , 'F' ,
            'G' , 'H' , 'I' , 'J' , 'K' , 'L' ,
            'M' , 'N' , 'O' , 'P' , 'Q' , 'R' ,
            'S' , 'T' , 'U' , 'V' , 'W' , 'X' ,
            'Y' , 'Z' , '#' , '@'
    };
    /**
     * 把10进制的数字转换成64进制
     * @param number
     * @return
     */
    public static String CompressNumber(long number) {
        int shift=6;		//2的6次方是64，所以shift是6
        char[] buf = new char[64];
        int charPos = 64;
        int radix = 1 << shift;
        long mask = radix - 1;
        do {
            buf[--charPos] = digits[(int)(number & mask)];
            number >>>= shift;
        } while (number != 0);
        return new String(buf, charPos, (64 - charPos));
    }
    /**
     * 把64进制的字符串转换成10进制
     * @param decompStr
     * @return
     */
    public static long UnCompressNumber(String decompStr)
    {
        long result=0;
        for (int i =  decompStr.length()-1; i >=0; i--) {
            if(i==decompStr.length()-1)
            {
                result+=getCharIndexNum(decompStr.charAt(i));
                continue;
            }
            for (int j = 0; j < digits.length; j++) {
                if(decompStr.charAt(i)==digits[j])
                {
                    result+=((long)j)<<6*(decompStr.length()-1-i);
                }
            }
        }
        return result;
    }

    /**
     * @param ch
     * @return
     */
    private static long getCharIndexNum(char ch) {
        int num = ((int) ch);
        if (num >= 48 && num <= 57) {
            return num - 48;
        } else if (num >= 97 && num <= 122) {
            return num - 87;
        } else if (num >= 65 && num <= 90) {
            return num - 29;
        } else if (num == 35) {
            return 62;
        } else if (num == 64) {
            return 63;
        }
        return 0;
    }
    /**
     * 把16进制的数字转换成64进制
     * @param guid
     * @return
     */
    public static String CompressGuid(String guid) {
        if(guid == null) return null;
        BigInteger number = new BigInteger(guid, 16);
        int shift = 6; // 2的6次方是64，所以shift是6
        char[] buf = new char[64];
        int charPos = 64;
        int radix = 1 << shift;
        long mask = radix - 1;
        BigInteger zero = BigInteger.valueOf(0);
        do {
            buf[--charPos] = digits[(number.and(BigInteger.valueOf(mask))).intValue()];
            number = number.shiftRight(shift);
        } while (!number.equals(zero));
        return new String(buf, charPos, (64 - charPos));
    }

    /**
     * 把64进制的字符串转换成16进制
     * @param decompStr
     * @return
     */
    public static String UnCompressGuid(String decompStr) {
        int shift = 6; // 2的6次方是64，所以shift是6
        BigInteger result = BigInteger.valueOf(0);
        for (int i = decompStr.length() - 1; i >= 0; i--) {
            if (i == decompStr.length() - 1) {
                result = result.add(BigInteger.valueOf(getCharIndexNum(decompStr.charAt(i))));
                continue;
            }
            for (int j = 0; j < digits.length; j++) {
                if (decompStr.charAt(i) == digits[j]) {
                    int tmpValue = shift * (decompStr.length() - 1 - i);
                    BigInteger valueBigInteger = BigInteger.valueOf(j).shiftLeft(tmpValue);
                    result = result.add(valueBigInteger);
                }
            }
        }
        return result.toString(16);
    }

}
