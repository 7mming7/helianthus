package com.ha.graph.execute;

import com.ha.exception.ExecutorManagerException;
import com.ha.executor.ExecutableFlow;
import com.ha.executor.ExecutableNode;
import com.ha.graph.flow.FlowService;
import com.ha.graph.node.NodeService;
import com.ha.graph.project.ProjectService;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: shuiqing
 * DateTime: 17/7/13 下午3:12
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Service
@Setter
@Getter
public class ExecuteLoaderServiceImpl implements IExecuteLoaderService {

    private Logger logger = LoggerFactory.getLogger(ExecuteLoaderServiceImpl.class);

    @Autowired
    private FlowService flowService;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private ProjectService projectService;


    @Override
    public void updateNode(ExecutableNode node) throws ExecutorManagerException {
        this.nodeService.update(nodeService.findOne(Long.parseLong(node.getId())));
    }

    @Override
    public void updateFlow(ExecutableFlow flow) throws ExecutorManagerException {
        this.flowService.update(flowService.findOne(Long.parseLong(flow.getFlowId())));
    }

}
