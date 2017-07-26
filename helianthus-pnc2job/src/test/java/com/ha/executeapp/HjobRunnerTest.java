package com.ha.executeapp;

import com.ha.TestCase;
import com.ha.execapp.HjobRunner;
import com.ha.executor.ExecutableFlow;
import com.ha.executor.ExecutableNode;
import com.ha.hjob.ProcessJob;
import com.ha.hjob.hjobType.HjobTypeManager;
import com.ha.utils.Props;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * User: shuiqing
 * DateTime: 17/7/14 上午10:12
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class HjobRunnerTest extends TestCase{

    private HjobTypeManager jobtypeManager;

    private File workingDir;

    @Before
    public void setUp() throws Exception {
        System.out.println("Create temp dir");
        workingDir = new File("_ImportTestDir_" + System.currentTimeMillis());
        if (workingDir.exists()) {
            FileUtils.deleteDirectory(workingDir);
        }
        workingDir.mkdirs();

        jobtypeManager =
                new HjobTypeManager(null);

    }

    @Test
    public void testBasicRun() {
        HjobRunner runner =
                createJobRunner("testJob", 1, false);

        runner.run();
    }

    private Props createProps(int sleepSec, boolean fail) {
        Props props = new Props();
        props.put("type", "command");

        props.put("seconds", sleepSec);
        props.put(ProcessJob.WORKING_DIR, workingDir.getPath());
        props.put("fail", String.valueOf(fail));
        props.put(ProcessJob.COMMAND, "df -h");

        return props;
    }

    private HjobRunner createJobRunner(String name, int t, boolean fail) {
        ExecutableFlow flow = new ExecutableFlow();
        ExecutableNode node = new ExecutableNode();
        node.setId(name);
        node.setParentFlow(flow);

        Props props = createProps(t, fail);
        node.setInputProps(props);

        HjobRunner runner = new HjobRunner(node, jobtypeManager);
        return runner;
    }
}
