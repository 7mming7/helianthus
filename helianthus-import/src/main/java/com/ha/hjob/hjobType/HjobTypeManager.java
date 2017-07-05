package com.ha.hjob.hjobType;

import com.ha.exception.HjobTypeManagerException;
import com.ha.hjob.JavaProcessJob;
import com.ha.hjob.NoopJob;
import com.ha.hjob.ProcessJob;
import com.ha.hjob.ScriptJob;
import com.ha.utils.Props;
import org.apache.log4j.Logger;
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
public class HjobTypeManager {

    private static final Logger logger = Logger.getLogger(HjobTypeManager.class);

    private HjobTypePluginSet pluginSet;

    private Props globalProperties;

    public HjobTypeManager(Props globalProperties) {
        this.globalProperties = globalProperties;

        loadDefaultTypes(pluginSet);
    }

    private void loadDefaultTypes(HjobTypePluginSet plugins)
            throws HjobTypeManagerException {
        logger.info("Loading plugin default job types");
        plugins.addPluginClass("command", ProcessJob.class);
        plugins.addPluginClass("javaprocess", JavaProcessJob.class);
        plugins.addPluginClass("noop", NoopJob.class);
        plugins.addPluginClass("script", ScriptJob.class);
    }

    /**
     * Public for test reasons. Will need to move tests to the same package
     */
    public synchronized HjobTypePluginSet getHJobTypePluginSet() {
        return this.pluginSet;
    }
}
