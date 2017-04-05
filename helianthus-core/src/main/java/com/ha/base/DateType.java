package com.ha.base;

/**
 * 日期类型藐视
 * User: shuiqing
 * DateTime: 17/4/5 下午6:22
 * Email: helianthus301@163.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public enum DateType {

    DAY(1, "日"),
    WEEK(2, "周"),
    MONTH(3, "月"),
    YEAR(4, "年");

    private final int value;	//类型值
    private final String name;	//名

    private DateType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    /**
     * 功能：根据类型值获取枚举类对象
     * @param value	类型值
     * @return
     */
    public static DateType findByValue(int value) {
        switch (value) {
            case 1:
                return DAY;
            case 2:
                return WEEK;
            case 3:
                return MONTH;
            case 4:
                return YEAR;
            default:
                return null;
        }
    }
}
