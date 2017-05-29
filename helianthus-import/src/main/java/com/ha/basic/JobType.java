package com.ha.basic;

/**
 * 任务类型
 * User: shuiqing
 * DateTime: 17/5/26 下午3:29
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public enum JobType {

    JAVA_JOB,
    MSG_JOB,
    SHELL_JOB,
    VSHELL,
    UNKOWN_JOB;

    public static JobType getJobType(String typeName){
        if(JAVA_JOB.toString().equalsIgnoreCase(typeName)){
            return JAVA_JOB;
        }
        if(MSG_JOB.toString().equalsIgnoreCase(typeName)){
            return MSG_JOB;
        }
        if(SHELL_JOB.toString().equalsIgnoreCase(typeName)){
            return SHELL_JOB;
        }
        if(VSHELL.toString().equalsIgnoreCase(typeName)){
            return VSHELL;
        }
        return UNKOWN_JOB;
    }
}
