package com.ha.hjob;

import com.ha.hjob.hjobType.HjobTypeManager;
import com.ha.utils.Props;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Properties;

/**
 * User: shuiqing
 * DateTime: 17/7/17 下午1:46
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class HtypeManagerTest {

    private static Logger log = LoggerFactory.getLogger(HtypeManagerTest.class);

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    private HjobTypeManager manager;

    public HtypeManagerTest() {
    }

    File workingDir;

    @Before
    public void setUp() throws Exception {
        manager = new HjobTypeManager();

        workingDir = temp.newFolder("TestProcess");
    }

    @Test
    public void testBuildClass() throws Exception {

        Properties prop = System.getProperties();
        String classPaths =
                String.format("'%s'", prop.getProperty("java.class.path", null));

        Props jobProps = new Props();
        jobProps.put("type", "javaprocess");
        jobProps.put("test", "test1");
        jobProps.put("pluginprops3", "4");
        jobProps.put(JavaProcessJob.WORKING_DIR, "/Users/gemingming/Downloads/import_test");
        jobProps.put(JavaProcessJob.JAVA_CLASS, "com.ha.hjob.WordCountLocal");
        jobProps.put("classpath", classPaths);
        Hjob job = manager.buildJobExecutor("javaprocess", jobProps);

        job.run();
    }

    @Test
    public void testBuildClass1() throws Exception {
        Props jobProps = new Props();
        jobProps.put("type", "command");
        jobProps.put("test", "test1");
        jobProps.put("pluginprops3", "4");
        jobProps.put(ProcessJob.WORKING_DIR, workingDir.getCanonicalPath());
        jobProps.put(ProcessJob.COMMAND, "df -h");
        Hjob job = manager.buildJobExecutor("command", jobProps);

        job.run();
    }
}