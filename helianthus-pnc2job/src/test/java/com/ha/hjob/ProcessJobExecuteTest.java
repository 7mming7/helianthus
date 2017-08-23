package com.ha.hjob;

import com.ha.TestCase;
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
 * DateTime: 17/7/5 下午3:37
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class ProcessJobExecuteTest extends TestCase {

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    private ProcessJob job = null;
    private Props props = new Props();

    private static Logger log = LoggerFactory.getLogger(ProcessJob.class);

    @Before
    public void setUp() throws Exception {
        File workingDir = temp.newFolder("TestProcess");

        props.put("fullPath", ".");
        props.put(CommonJobProperties.PROJECT_NAME, "test_project");
        props.put(CommonJobProperties.FLOW_ID, "test_flow");
        props.put(CommonJobProperties.JOB_ID, "test_job");
        props.put(ProcessJob.WORKING_DIR, workingDir.getCanonicalPath());
        props.put("type", "command");

        job = new ProcessJob("TestProcess",null,props);
    }

    @Test
    public void testOneUnixCommand() throws Exception {
        // Initialize the Props
        props.put(ProcessJob.COMMAND, "df -h");
        job.run();
    }
}