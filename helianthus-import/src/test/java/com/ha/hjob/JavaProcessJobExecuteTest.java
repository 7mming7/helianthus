package com.ha.hjob;

import com.ha.base.CommonJobProperties;
import com.ha.utils.Props;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * User: shuiqing
 * DateTime: 17/7/6 上午10:06
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class JavaProcessJobExecuteTest {

    private static Logger log = LoggerFactory.getLogger(JavaProcessJob.class);

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    private JavaProcessJob job = null;
    private Props props = new Props();

    private static String classPaths;

    @BeforeClass
    public static void init() throws IOException {
        Properties prop = System.getProperties();
        classPaths =
                String.format("'%s'", prop.getProperty("java.class.path", null));

    }

    @Before
    public void setUp() throws Exception {
        File workingDir = temp.newFolder("TestProcess1");
        System.out.println("workingDir->>" + temp.getRoot().getCanonicalPath());
        props.put("fullPath", ".");
        props.put(CommonJobProperties.PROJECT_NAME, "test_project");
        props.put(CommonJobProperties.FLOW_ID, "test_flow");
        props.put(CommonJobProperties.JOB_ID, "test_job");
        props.put(JavaProcessJob.WORKING_DIR, workingDir.getCanonicalPath());
        props.put("type", "java");

        job = new JavaProcessJob("TestProcess", props);
    }

    @Test
    public void test() throws Exception {
        // Initialize the Props
        props.put(JavaProcessJob.JAVA_CLASS, "com.ha.hjob.WordCountLocal");
        props.put("classpath", classPaths);
        job.run();
    }
}
