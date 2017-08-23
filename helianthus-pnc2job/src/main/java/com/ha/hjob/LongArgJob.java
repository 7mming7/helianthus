package com.ha.hjob;

/**
 * User: shuiqing
 * DateTime: 17/7/4 下午2:02
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */

import com.ha.hjob.process.ImportProcess;
import com.ha.hjob.process.ImportProcessBuilder;
import com.ha.utils.Props;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * A job that passes all the job properties as command line arguments in "long"
 * format, e.g. --key1 value1 --key2 value2 ...
 */
public abstract class LongArgJob extends ProcessJob {

    private static final long KILL_TIME_MS = 5000;
    private final ImportProcessBuilder builder;
    private volatile ImportProcess process;

    public LongArgJob(String jobid, String[] command, Props sysProps, Props jobProps) {
        this(jobid, command, sysProps, jobProps, new HashSet<String>(0));
    }

    public LongArgJob(String jobid, String[] command, Props sysProps, Props jobProp, Set<String> suppressedKeys) {
        super(jobid, sysProps, jobProp);

        this.builder =
                new ImportProcessBuilder(command)
                        .setEnv(getJobProps().getMapByPrefix(ENV_PREFIX))
                        .setWorkingDir(getCwd()).setLogger(getLog());
        appendProps(suppressedKeys);
    }

    public void run() throws Exception {

        long startMs = System.currentTimeMillis();
        info("Command: " + builder.getCommandString());
        if (builder.getEnv().size() > 0) {
            info("Environment variables: " + builder.getEnv());
        }
        info("Working directory: " + builder.getWorkingDir());

        File[] propFiles = initPropsFiles();

        // print out the Job properties to the job log.
        this.logJobProperties();

        boolean success = false;
        this.process = builder.build();
        try {
            this.process.run();
            success = true;
        } catch (Exception e) {
            for (File file : propFiles) {
                if (file != null && file.exists()) {
                    file.delete();
                }
            }
            throw new RuntimeException(e);
        } finally {
            this.process = null;
            info("Process completed " + (success ? "successfully" : "unsuccessfully")
                    + " in " + ((System.currentTimeMillis() - startMs) / 1000)
                    + " seconds.");
        }

        for (File file : propFiles) {
            if (file != null && file.exists()) {
                file.delete();
            }
        }
    }

    @Override
    public void cancel() throws InterruptedException {
        if (process == null) {
            throw new IllegalStateException("Not started.");
        }

        boolean killed = process.softKill(KILL_TIME_MS, TimeUnit.MILLISECONDS);
        if (!killed) {
            warn("Kill with signal TERM failed. Killing with KILL signal.");
            process.hardKill();
        }
    }

    /**
     * This gives access to the process builder used to construct the process. An
     * overriding class can use this to add to the command being executed.
     */
    protected ImportProcessBuilder getBuilder() {
        return this.builder;
    }

    private void appendProps(Set<String> suppressed) {
        ImportProcessBuilder builder = this.getBuilder();
        Props props = getJobProps();
        for (String key : props.getKeySet()) {
            if (!suppressed.contains(key)) {
                builder.addArg("--" + key, props.get(key));
            }
        }
    }
}
