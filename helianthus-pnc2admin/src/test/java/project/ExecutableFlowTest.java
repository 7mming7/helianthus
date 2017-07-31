package project;

import base.TestCase;
import com.ha.executor.ExecutableFlow;
import com.ha.graph.flow.Flow;
import com.ha.graph.project.Project;
import com.ha.graph.project.ProjectService;
import com.ha.project.DirectoryFlowLoader;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Autowired
    private ProjectService projectService;

    @Autowired
    private DirectoryFlowLoader directoryFlowLoader;

    private Project project;

    @Before
    public void setUp() throws Exception {

        project = projectService.findOne(1l);

        directoryFlowLoader.loadProjectFlow(project);
        Assert.assertEquals(0, directoryFlowLoader.getErrors().size());

        project.setFlows(directoryFlowLoader.getFlowMap());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testExecutorFlowCreation() throws Exception {
        Flow flow = project.getFlow("1");
        Assert.assertNotNull(flow);

        ExecutableFlow exFlow = new ExecutableFlow(flow);
    }
}