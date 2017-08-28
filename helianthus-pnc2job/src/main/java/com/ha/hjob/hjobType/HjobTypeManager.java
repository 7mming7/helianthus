package com.ha.hjob.hjobType;

import com.ha.exception.HjobTypeManagerException;
import com.ha.exception.JobExecutionException;
import com.ha.hjob.*;
import com.ha.util.Utils;
import com.ha.utils.Props;
import com.ha.utils.PropsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * User: shuiqing
 * DateTime: 17/7/5 上午10:29
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Component
public class HjobTypeManager {

    private static final Logger logger = LoggerFactory.getLogger(HjobTypeManager.class);

    private HjobTypePluginSet pluginSet;

    private Props globalProperties;

    public HjobTypeManager() {
        loadPlugins();
    }

    public void loadPlugins() throws HjobTypeManagerException {
        HjobTypePluginSet plugins = new HjobTypePluginSet();

        loadDefaultTypes(plugins);

        // Swap the plugin set. If exception is thrown, then plugin isn't swapped.
        synchronized (this) {
            pluginSet = plugins;
        }
    }

    private void loadDefaultTypes(HjobTypePluginSet plugins)
            throws HjobTypeManagerException {
        logger.info("Loading plugin default job types");
        plugins.addPluginClass("command", ProcessJob.class);
        plugins.addPluginClass("javaprocess", JavaProcessJob.class);
        plugins.addPluginClass("noop", NoopJob.class);
        plugins.addPluginClass("script", ScriptJob.class);
    }

    public Hjob buildJobExecutor(String jobId, Props sysProps, Props jobProps)
            throws HjobTypeManagerException {
        // This is final because during build phase, you should never need to swap
        // the pluginSet for safety reasons
        final HjobTypePluginSet pluginSet = getHJobTypePluginSet();

        Hjob hjob = null;
        try {
            String jobType = jobProps.getString("type");
            if (jobType == null || jobType.length() == 0) {
            /* throw an exception when job name is null or empty */
                throw new JobExecutionException(String.format(
                        "The 'type' parameter for job[%s] is null or empty", jobProps));
            }

            logger.info("Building " + jobType + " job executor. ");

            Class<? extends Object> executorClass = pluginSet.getPluginClass(jobType);
            if (executorClass == null) {
                throw new JobExecutionException(String.format("Job type '" + jobType
                                + "' is unrecognized. Could not construct job[%s] of type[%s].",
                        jobProps, jobType));
            }

            Props pluginJobProps = pluginSet.getPluginJobProps(jobType);
            // For default jobtypes, even though they don't have pluginJobProps configured,
            // they still need to load properties from common.properties file if it's present
            // because common.properties file is global to all jobtypes.
            if(pluginJobProps == null) {
                pluginJobProps = pluginSet.getCommonPluginJobProps();
            }
            if (pluginJobProps != null) {
                for (String k : pluginJobProps.getKeySet()) {
                    if (!jobProps.containsKey(k)) {
                        jobProps.put(k, pluginJobProps.get(k));
                    }
                }
            }
            jobProps = PropsUtils.resolveProps(jobProps);

            //@TODO 需要设置sys props.
            hjob = (Hjob) Utils.callConstructor(executorClass, jobId, sysProps, jobProps);
        } catch (Exception e) {
            logger.error("Failed to build job executor for job " + jobId
                    + e.getMessage());
            throw new HjobTypeManagerException("Failed to build job executor for job "
                    + jobId, e);
        } catch (Throwable t) {
            logger.error(
                    "Failed to build job executor for job " + jobId + t.getMessage(), t);
            throw new HjobTypeManagerException("Failed to build job executor for job "
                    + jobId, t);
        }

        return hjob;
    }

    /**
     * Public for test reasons. Will need to move tests to the same package
     */
    public synchronized HjobTypePluginSet getHJobTypePluginSet() {
        return this.pluginSet;
    }
}
