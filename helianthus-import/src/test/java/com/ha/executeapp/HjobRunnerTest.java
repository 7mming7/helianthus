package com.ha.executeapp;

import com.ha.execapp.HjobRunner;
import com.ha.executor.ExecutableFlow;
import com.ha.executor.ExecutableNode;
import com.ha.hjob.hjobType.HjobTypeManager;
import com.ha.utils.Props;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

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
public class HjobRunnerTest {

    private HjobTypeManager jobtypeManager;

    @Before
    public void setUp() throws Exception {
        jobtypeManager =
                new HjobTypeManager(null);

    }

    @Ignore
    @Test
    public void testBasicRun() {
        HjobRunner runner =
                createJobRunner("testJob");
        ExecutableNode node = runner.getNode();

        runner.run();

    }

    private Props createProps(int sleepSec, boolean fail) {
        Props props = new Props();
        props.put("type", "java");
        props.put("seconds", sleepSec);
        props.put("fail", String.valueOf(fail));

        return props;
    }

    private HjobRunner createJobRunner(String name) {
        ExecutableFlow flow = new ExecutableFlow();
        ExecutableNode node = new ExecutableNode();
        node.setId(name);
        node.setParentFlow(flow);

        HjobRunner runner = new HjobRunner(node, jobtypeManager);
        return runner;
    }
}
