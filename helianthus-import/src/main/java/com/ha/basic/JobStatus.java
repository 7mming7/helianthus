package com.ha.basic;

/**
 * 任务状态
 * User: shuiqing
 * DateTime: 17/5/24 下午3:42
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public enum JobStatus {

    READY(0),
    SUCCEEDED(10),
    DELAY(20),
    FAILED(30),
    KILLED(40),
    SKIPPED(50),
    QUEUED(60);

    private int numVal;

    JobStatus(int numVal) {
        this.numVal = numVal;
    }

    public int getNumVal() {
        return numVal;
    }

    public static JobStatus fromInteger(int x) {
        switch (x) {
            case 0:
                return READY;
            case 10:
                return SUCCEEDED;
            case 20:
                return DELAY;
            case 30:
                return FAILED;
            case 40:
                return KILLED;
            case 50:
                return SKIPPED;
            case 60:
                return QUEUED;
            default:
                return SUCCEEDED;
        }
    }
}