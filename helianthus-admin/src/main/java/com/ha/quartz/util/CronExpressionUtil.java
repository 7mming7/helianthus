package com.ha.quartz.util;

import org.apache.commons.lang3.StringUtils;
import org.quartz.CronScheduleBuilder;

/**
 * quartz cron 表达式校验
 * User: shuiqing
 * DateTime: 17/4/20 下午3:27
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class CronExpressionUtil {

    public static boolean isValidExpression(String cronExpression) {
        try {
            if (StringUtils.isBlank(cronExpression)) return false;
            CronScheduleBuilder cronBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static String convertToSpringCron(String cronExpression) {
        String[] cronArray = cronExpression.split(" ");
        if (cronArray.length < 6) return null;
        if (!"?".equals(cronArray[3])) cronArray[5] = "?";
        else cronArray[3] = "?";

        return StringUtils.join(cronArray, " ");
    }
}
