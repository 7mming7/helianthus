package com.ha.trigger;

import com.ha.basic.ElasticJob;
import org.quartz.SchedulerException;

/**
 * User: shuiqing
 * DateTime: 17/5/27 下午2:24
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface ImportTrigger {

    public ImportScheduler build(ElasticJob job) throws SchedulerException;

    public void retrigger(ImportScheduler scheduler,ElasticJob job) throws SchedulerException;

}
