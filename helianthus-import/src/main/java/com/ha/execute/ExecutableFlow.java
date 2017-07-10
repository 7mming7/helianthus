package com.ha.execute;

import com.ha.graph.Edge;
import com.ha.graph.flow.Flow;
import com.ha.graph.node.Node;
import com.ha.graph.project.Project;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 可执行的flow
 * User: shuiqing
 * DateTime: 17/7/10 下午2:33
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Setter
@Getter
public class ExecutableFlow extends ExecutableNode {

    public static final String FLOW_ID_PARAM = "flowId";
    public static final String NODES_PARAM = "nodes";

    private HashMap<String, ExecutableNode> executableNodes =
            new HashMap<String, ExecutableNode>();
    private ArrayList<String> startNodes;
    private ArrayList<String> endNodes;

    private String flowId;

    public ExecutableFlow(Project project, Node node, Flow flow,
                              ExecutableFlow parent) {
        super(node, parent);

        setFlow(project, flow);
    }

    protected void setFlow(Project project, Flow flow) {
        this.flowId = flow.getId().toString();

        for (Node node : flow.getNodes()) {
            String id = node.getId().toString();
            ExecutableNode exNode = new ExecutableNode(node, this);
            executableNodes.put(id, exNode);
        }

        for (Edge edge : flow.getEdges()) {
            ExecutableNode sourceNode = executableNodes.get(edge.getSourceId());
            ExecutableNode targetNode = executableNodes.get(edge.getTargetId());

            if (sourceNode == null) {
                System.out.println("Source node " + edge.getSourceId()
                        + " doesn't exist");
            }
            sourceNode.addOutNode(edge.getTargetId());
            targetNode.addInNode(edge.getSourceId());
        }
    }
}
