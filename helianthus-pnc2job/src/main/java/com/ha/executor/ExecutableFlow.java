package com.ha.executor;

import com.ha.graph.flow.Edge;
import com.ha.graph.flow.Flow;
import com.ha.graph.node.Node;
import com.ha.graph.project.Project;
import com.ha.utils.TypedMapWrapper;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public ExecutableFlow() {
    }

    public ExecutableFlow(Flow flow) {
        setFlow(flow);
    }

    public ExecutableFlow(Node node, Flow flow,
                              ExecutableFlow parent) {
        super(node, parent);

        setFlow(flow);
    }

    protected void setFlow(Flow flow) {
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

    public List<ExecutableNode> getExecutableNodes() {
        return new ArrayList<ExecutableNode>(executableNodes.values());
    }

    public ExecutableNode getExecutableNode(String id) {
        return executableNodes.get(id);
    }

    public List<String> getStartNodes() {
        if (startNodes == null) {
            startNodes = new ArrayList<String>();
            for (ExecutableNode node : executableNodes.values()) {
                if (node.getInNodes().isEmpty()) {
                    startNodes.add(node.getId());
                }
            }
        }

        return startNodes;
    }

    public List<String> getEndNodes() {
        if (endNodes == null) {
            endNodes = new ArrayList<String>();
            for (ExecutableNode node : executableNodes.values()) {
                if (node.getOutNodes().isEmpty()) {
                    endNodes.add(node.getId());
                }
            }
        }

        return endNodes;
    }

    public void reEnableDependenps(ExecutableNode... nodes) {
        for (ExecutableNode node : nodes) {
            for (String dependent : node.getOutNodes()) {
                ExecutableNode dependentNode = getExecutableNode(dependent);

                if (dependentNode.getStatus() == ExecuteStatus.KILLED) {
                    dependentNode.setStatus(ExecuteStatus.READY);
                    reEnableDependenps(dependentNode);

                    if (dependentNode instanceof ExecutableFlow) {

                        ((ExecutableFlow) dependentNode).reEnableDependenps();
                    }
                } else if (dependentNode.getStatus() == ExecuteStatus.SKIPPED) {
                    dependentNode.setStatus(ExecuteStatus.DISABLED);
                    reEnableDependenps(dependentNode);
                }
            }
        }
    }

    public ExecutableNode getExecutableNodePath(String ids) {
        String[] split = ids.split(":");
        return getExecutableNodePath(split);
    }

    public ExecutableNode getExecutableNodePath(String... ids) {
        return getExecutableNodePath(this, ids, 0);
    }

    private ExecutableNode getExecutableNodePath(ExecutableFlow flow,
                                                 String[] ids, int currentIdIdx) {
        ExecutableNode node = flow.getExecutableNode(ids[currentIdIdx]);
        currentIdIdx++;

        if (node == null) {
            return null;
        }

        if (ids.length == currentIdIdx) {
            return node;
        } else if (node instanceof ExecutableFlow) {
            return getExecutableNodePath((ExecutableFlow) node, ids, currentIdIdx);
        } else {
            return null;
        }

    }

    /**
     * Only returns true if the status of all finished nodes is true.
     *
     * @return
     */
    public boolean isFlowFinished() {
        for (String end : getEndNodes()) {
            ExecutableNode node = getExecutableNode(end);
            if (!ExecuteStatus.isStatusFinished(node.getStatus())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Finds all jobs which are ready to run. This occurs when all of its
     * dependency nodes are finished running.
     *
     * It will also return any subflow that has been completed such that the
     * FlowRunner can properly handle them.
     *
     * @return
     */
    public List<ExecutableNode> findNextJobsToRun() {
        ArrayList<ExecutableNode> jobsToRun = new ArrayList<ExecutableNode>();

        if (isFlowFinished() && !ExecuteStatus.isStatusFinished(getStatus())) {
            jobsToRun.add(this);
        } else {
            nodeloop: for (ExecutableNode node : executableNodes.values()) {
                if (ExecuteStatus.isStatusFinished(node.getStatus())) {
                    continue;
                }

                if ((node instanceof ExecutableFlow)
                        && ExecuteStatus.isStatusRunning(node.getStatus())) {
                    // If the flow is still running, we traverse into the flow
                    jobsToRun.addAll(((ExecutableFlow) node).findNextJobsToRun());
                } else if (ExecuteStatus.isStatusRunning(node.getStatus())) {
                    continue;
                } else {
                    for (String dependency : node.getInNodes()) {
                        // We find that the outer-loop is unfinished.
                        if (!ExecuteStatus.isStatusFinished(getExecutableNode(dependency)
                                .getStatus())) {
                            continue nodeloop;
                        }
                    }

                    jobsToRun.add(node);
                }
            }
        }

        return jobsToRun;
    }

    @Override
    protected void fillMapFromExecutable(Map<String, Object> flowObjMap) {
        super.fillMapFromExecutable(flowObjMap);

        flowObjMap.put(FLOW_ID_PARAM, flowId);

        ArrayList<Object> nodes = new ArrayList<Object>();
        for (ExecutableNode node : executableNodes.values()) {
            nodes.add(node.toObject());
        }
        flowObjMap.put(NODES_PARAM, nodes);
    }

    @Override
    public void fillExecutableFromMapObject(
            TypedMapWrapper<String, Object> flowObjMap) {
        super.fillExecutableFromMapObject(flowObjMap);

        this.flowId = flowObjMap.getString(FLOW_ID_PARAM);
        List<Object> nodes = flowObjMap.getList(NODES_PARAM);

        if (nodes != null) {
            for (Object nodeObj : nodes) {
                @SuppressWarnings("unchecked")
                Map<String, Object> nodeObjMap = (Map<String, Object>) nodeObj;

                ExecutableNode exJob = new ExecutableNode();
                exJob.fillExecutableFromMapObject(nodeObjMap);
                exJob.setParentFlow(this);

                executableNodes.put(exJob.getId(), exJob);
            }
        }
    }

    public static ExecutableFlow createExecutableFlowFromObject(Object obj) {
        ExecutableFlow exFlow = new ExecutableFlow();
        HashMap<String, Object> flowObj = (HashMap<String, Object>) obj;
        exFlow.fillExecutableFromMapObject(flowObj);

        return exFlow;
    }
}
