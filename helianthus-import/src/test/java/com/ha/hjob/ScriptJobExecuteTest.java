package com.ha.hjob;

import com.ha.base.CommonJobProperties;
import com.ha.utils.Props;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * User: shuiqing
 * DateTime: 17/7/6 上午11:23
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class ScriptJobExecuteTest {

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    private ScriptJob job = null;
    private Props props = new Props();

    private static Logger log = LoggerFactory.getLogger(ProcessJob.class);

    @Before
    public void setUp() throws Exception {
        File workingDir = temp.newFolder("TestProcess");

        props.put("fullPath", ".");
        props.put(CommonJobProperties.PROJECT_NAME, "test_project");
        props.put(CommonJobProperties.FLOW_ID, "test_flow");
        props.put(CommonJobProperties.JOB_ID, "test_job");
        props.put(ScriptJob.WORKING_DIR, workingDir.getCanonicalPath());
        props.put("type", "script");
        props.put("script", "script");
        props.put("executable", "true");

        job = new ScriptJob("TestProcess", props);
    }

    @Test
    public void testOneUnixCommand() throws Exception {
        // Initialize the Props
        job.run();
    }
}
