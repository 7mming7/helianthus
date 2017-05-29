package com.ha.basic;

/**
 * 解析器类型
 * User: shuiqing
 * DateTime: 17/5/22 下午2:38
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public enum ParserType {

    JOB(1),
    STEP(2);

    private ParserType(int value) {
        this.value = value;
    }

    private final int value;	//类型值

    public static ParserType findByValue(int value) {
        switch (value) {
            case 1:
                return JOB;
            case 2:
                return STEP;
            default:
                return null;
        }
    }
}
