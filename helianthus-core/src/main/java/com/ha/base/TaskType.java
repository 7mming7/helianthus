package com.ha.base;

/**
 * 任务类型
 * User: shuiqing
 * DateTime: 17/4/6 下午3:15
 * Email: helianthus301@163.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public enum TaskType {

    HADOOP(1),
    STORM(2),
    SPARK(3);

    private final int value;	//类型值

    private TaskType(int value) {
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
    public static TaskType findByValue(int value) {
        switch (value) {
            case 1:
                return HADOOP;
            case 2:
                return STORM;
            case 3:
                return SPARK;
            default:
                return null;
        }
    }
}
