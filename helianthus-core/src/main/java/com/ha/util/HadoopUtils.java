package com.ha.util;

import com.google.common.base.Splitter;
import com.ha.hadoop.HadoopCompat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.TaskInputOutputContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: shuiqing
 * DateTime: 17/3/30 下午2:25
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class HadoopUtils {

    private static final Logger LOG = LoggerFactory.getLogger(HadoopUtils.class);
    private static final Splitter COMMA_SPLITTER = Splitter.on(',');

    public static Counter getCounter(JobContext ctx, String group, String counter) {
        if (ctx instanceof TaskInputOutputContext<?, ?, ?, ?>) {
            Counter c = HadoopCompat.getCounter((TaskInputOutputContext<?, ?, ?, ?>) ctx,
                    group, counter);
            if (c != null) {
                return c;
            }
        }
        String name = group + ":" + counter;
        LOG.warn("Using a dummy counter for " + name + " because it does not already exist.");
        return HadoopCompat.newGenericCounter(name, name, 0);
    }

    public static void setClassConf(Configuration conf,
                                    String         configKey,
                                    Class<?>       clazz) {
        String existingClass = conf.get(configKey);
        String className = clazz.getName();

        if (existingClass != null && !existingClass.equals(className)) {
            throw new RuntimeException(
                    "Already registered a different thriftClass for "
                            + configKey
                            + ". old: " + existingClass
                            + " new: " + className);
        } else {
            conf.set(configKey, className);
        }
    }
}
