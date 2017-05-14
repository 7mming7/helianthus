package com.ha.api;

/**
 * 作业类型
 * User: shuiqing
 * DateTime: 17/5/9 下午2:09
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public enum JobType {

    SIMPLE(1),
    DATAFLOW(2),
    SCRIPT(3);

    private final int value;	//类型值

    private JobType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    /**
     * 功能：根据类型值获取枚举类对象
     * @param value	类型值
     * @return
     */
    public static JobType findByValue(int value) {
        switch (value) {
            case 1:
                return SIMPLE;
            case 2:
                return DATAFLOW;
            case 3:
                return SCRIPT;
            default:
                return null;
        }
    }
}
