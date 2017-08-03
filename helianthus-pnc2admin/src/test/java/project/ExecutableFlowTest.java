package project;

import base.TestCase;
import com.alibaba.druid.support.json.JSONUtils;
import com.ha.execapp.HflowRunner;
import com.ha.executor.ExecutableFlow;
import com.ha.graph.flow.Flow;
import com.ha.graph.flow.FlowService;
import com.ha.graph.node.NodeService;
import com.ha.graph.project.Project;
import com.ha.graph.project.ProjectService;
import com.ha.hjob.hjobType.HjobTypeManager;
import com.ha.project.DirectoryFlowLoader;
import com.ha.util.JsonUtil;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.Map;

/**
 * User: shuiqing
 * DateTime: 17/7/28 下午2:44
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class ExecutableFlowTest extends TestCase {

    private HjobTypeManager jobtypeManager;

    private File workingDir;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private FlowService flowService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private DirectoryFlowLoader directoryFlowLoader;

    private Project project;

    @Before
    public void setUp() throws Exception {

        System.out.println("Create temp dir");
        workingDir = new File("_ImportTestDir_" + System.currentTimeMillis());
        if (workingDir.exists()) {
            FileUtils.deleteDirectory(workingDir);
        }
        workingDir.mkdirs();

        jobtypeManager =
                new HjobTypeManager();

        project = projectService.findOne(1l);

        directoryFlowLoader.loadProjectFlow(project);
        Assert.assertEquals(0, directoryFlowLoader.getErrors().size());

        project.setFlows(directoryFlowLoader.getFlowMap());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void exec1Normal() throws Exception {
        HflowRunner runner = createFlowRunner();

        Assert.assertTrue(!runner.isKilled());
        runner.run();
    }

    private HflowRunner createFlowRunner() throws Exception {
        ExecutableFlow exFlow = fetchExecutorFlowCreation();
        HflowRunner runner =
                new HflowRunner(exFlow, jobtypeManager,null);

        return runner;
    }

    public ExecutableFlow fetchExecutorFlowCreation() throws Exception {
        Flow flow = project.getFlow("1");
        Assert.assertNotNull(flow);

        ExecutableFlow exFlow = new ExecutableFlow(flow);

        /*Object obj = exFlow.toObject();
        String exFlowJSON = JsonUtil.toJSON(obj);
        @SuppressWarnings("unchecked")
        Map<String, Object> flowObjMap =
                (Map<String, Object>) JsonUtil.parseJSONFromString(exFlowJSON);

        ExecutableFlow parsedExFlow =
                ExecutableFlow.createExecutableFlowFromObject(flowObjMap);*/

        return exFlow;
    }
}