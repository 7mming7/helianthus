package hjob;

import base.TestCase;
import com.ha.execapp.HflowRunner;
import com.ha.execapp.HjobRunner;
import com.ha.executor.ExecutableFlow;
import com.ha.graph.flow.Flow;
import com.ha.graph.flow.FlowService;
import com.ha.graph.node.NodeService;
import com.ha.hjob.hjobType.HjobTypeManager;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

/**
 * User: shuiqing
 * DateTime: 17/7/28 上午10:54
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class HflowRunnerTest extends TestCase{

    private HjobTypeManager jobtypeManager;

    private File workingDir;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private FlowService flowService;

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
    public void exec1Normal() throws Exception {
        HflowRunner runner = createFlowRunner(1l);

        Assert.assertTrue(!runner.isKilled());
        runner.run();
    }

    private HflowRunner createFlowRunner(Long flowId) throws Exception {
        Flow flow = flowService.findOne(flowId);

        ExecutableFlow executableFlow = new ExecutableFlow(flow);

        HflowRunner runner =
                new HflowRunner(executableFlow, jobtypeManager,null);

        return runner;
    }
}
