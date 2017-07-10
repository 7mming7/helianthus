package com.ha.hjob;

/**
 * User: shuiqing
 * DateTime: 17/7/4 下午2:18
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */

import com.google.common.collect.ImmutableSet;
import com.ha.utils.Props;
import org.slf4j.Logger;

/**
 * A script job issues a command of the form [EXECUTABLE] [SCRIPT] --key1 val1
 * ... --key2 val2 executable -- the interpretor command to execute script --
 * the script to pass in (requried)
 *
 */
public class ScriptJob extends LongArgJob {

    private static final String DEFAULT_EXECUTABLE_KEY = "executable";
    public static final String COMMAND = "script";

    @Override
    public String getCommand() {
        return COMMAND;
    }

    public ScriptJob(String jobid, Props sysProps, Props jobProps, Logger log) {
        super(jobid,
                new String[] {
                        jobProps.getString(DEFAULT_EXECUTABLE_KEY),
                        jobProps.getString(COMMAND)
                },
                sysProps,
                jobProps,
                log,
                ImmutableSet.of(DEFAULT_EXECUTABLE_KEY, COMMAND, JOB_TYPE));
    }

}