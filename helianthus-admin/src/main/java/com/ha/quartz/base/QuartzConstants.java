package com.ha.quartz.base;

import java.util.HashMap;
import java.util.Map;

/**
 * quartz 常量
 * User: shuiqing
 * DateTime: 17/4/20 下午2:20
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class QuartzConstants {

    public static final String TRIGGERNAME = "triggerName";
    public static final String TRIGGERGROUP = "triggerGroup";
    public static final String STARTTIME = "startTime";
    public static final String ENDTIME = "endTime";
    public static final String REPEATCOUNT = "repeatCount";
    public static final String REPEATINTERVEL = "repeatInterval";

    public static final Map<String,String> status = new HashMap<String,String>();

    static{
        status.put("ACQUIRED", "运行中");
        status.put("PAUSED", "暂停中");
        status.put("WAITING", "等待中");
    }

    public static String JOB_PARAM_KEY = "scheduleJob";
}
