package com.ha.project;

import com.ha.base.CommonJobProperties;
import com.ha.entity.search.MatchType;
import com.ha.entity.search.Searchable;
import com.ha.executor.ExecutableNode;
import com.ha.graph.flow.Edge;
import com.ha.graph.flow.Flow;
import com.ha.graph.flow.FlowService;
import com.ha.graph.node.Node;
import com.ha.graph.node.NodeService;
import com.ha.graph.project.Project;
import com.ha.graph.project.ProjectService;
import com.ha.util.StringUtils;
import com.ha.utils.Props;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * User: shuiqing
 * DateTime: 17/7/28 下午1:52
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Setter
@Getter
@Component
public class DirectoryFlowLoader {

    public static final Logger logger = LoggerFactory.getLogger(DirectoryFlowLoader.class);

    private Props props;
    private HashSet<String> rootNodes;
    private HashMap<String, Flow> flowMap;
    private HashMap<String, Node> nodeMap;
    private HashMap<String, Map<String, Edge>> nodeDependencies;
    private HashMap<String, Props> jobPropsMap;

    private Set<String> errors;
    private Set<String> duplicateJobs;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private FlowService flowService;

    @Autowired
    private NodeService nodeService;

    /**
     * Creates a new DirectoryFlowLoader.
     */
    public DirectoryFlowLoader() {
    }

    /**
     * Returns the flow map constructed from the loaded flows.
     *
     * @return Map of flow name to Flow.
     */
    public Map<String, Flow> getFlowMap() {
        return flowMap;
    }

    /**
     * Returns errors caught when loading flows.
     *
     * @return Set of error strings.
     */
    public Set<String> getErrors() {
        return errors;
    }

    /**
     * Returns job properties.
     *
     * @return Map of job name to properties.
     */
    public Map<String, Props> getJobProps() {
        return jobPropsMap;
    }

    public void loadProjectFlow(Project project) {
        jobPropsMap = new HashMap<String, Props>();
        nodeMap = new HashMap<String, Node>();
        flowMap = new HashMap<String, Flow>();
        errors = new HashSet<String>();
        duplicateJobs = new HashSet<String>();
        nodeDependencies = new HashMap<String, Map<String, Edge>>();
        rootNodes = new HashSet<String>();

        //Load project nodes.
        loadProjectNodes(project);

        // Create edges and find missing dependencies.
        resolveDependencies();

        // Create the flows.
        buildFlowsFromDependencies();
    }

    private void loadProjectNodes(Project project){

        Searchable searchable = Searchable.newSearchable();
        searchable.addSearchFilter("project.id", MatchType.EQ, project.getId());
        List<Flow> flowList = flowService.findAllWithNoPageNoSort(searchable);
        for(Flow flow:flowList){
            flowMap.put(flow.getId().toString(),flow);
        }
        List<Node> nodeList = nodeService.findAllWithNoPageNoSort(searchable);
        for(Node node:nodeList){
            ExecutableNode executableNode = new ExecutableNode(node);
            // Force root node
            if (executableNode.getInputProps().getBoolean(CommonJobProperties.ROOT_NODE, false)) {
                rootNodes.add(executableNode.getId());
            }

            jobPropsMap.put(executableNode.getId(), executableNode.getInputProps());
            nodeMap.put(executableNode.getId(), node);
        }
    }

    private void resolveDependencies() {
        // Add all the in edges and out edges. Catch bad dependencies and self
        // referrals. Also collect list of nodes who are parents.
        for (Node node : nodeMap.values()) {

            List<String> dependencyList = new LinkedList<String>();
            String nodeDepends = node.getDepends();
            if(!StringUtils.isBlank(nodeDepends)){
                for(String dp:nodeDepends.split(CommonJobProperties.NODE_DEPNEDS_SPLIT)){
                    dependencyList.add(dp);
                }
            }

            if (dependencyList != null) {
                Map<String, Edge> dependencies = nodeDependencies.get(node.getId());
                if (dependencies == null) {
                    dependencies = new HashMap<String, Edge>();

                    for (String dependencyName : dependencyList) {
                        dependencyName =
                                dependencyName == null ? null : dependencyName.trim();
                        if (dependencyName == null || dependencyName.isEmpty()) {
                            continue;
                        }

                        Edge edge = new Edge(dependencyName, node.getId().toString());
                        Node dependencyNode = nodeMap.get(dependencyName);
                        if (dependencyNode == null) {
                            if (duplicateJobs.contains(dependencyName)) {
                                edge.setError("Ambiguous Dependency. Duplicates found.");
                                dependencies.put(dependencyName, edge);
                                errors.add(node.getId() + " has ambiguous dependency "
                                        + dependencyName);
                            } else {
                                edge.setError("Dependency not found.");
                                dependencies.put(dependencyName, edge);
                                errors.add(node.getId() + " cannot find dependency "
                                        + dependencyName);
                            }
                        } else if (dependencyNode == node) {
                            // We have a self cycle
                            edge.setError("Self cycle found.");
                            dependencies.put(dependencyName, edge);
                            errors.add(node.getId() + " has a self cycle");
                        } else {
                            dependencies.put(dependencyName, edge);
                        }
                    }

                    if (!dependencies.isEmpty()) {
                        nodeDependencies.put(node.getId().toString(), dependencies);
                    }
                }
            }
        }
    }

    /**
     * build flows on depends node or flow.
     */
    private void buildFlowsFromDependencies() {
        // Find all root nodes by finding ones without dependents.
        HashSet<String> nonRootNodes = new HashSet<String>();
        for (Map<String, Edge> edges : nodeDependencies.values()) {
            for (String sourceId : edges.keySet()) {
                nonRootNodes.add(sourceId);
            }
        }

        // Now create flows. Bad flows are marked invalid
        Set<String> visitedNodes = new HashSet<String>();
        for (Node base : nodeMap.values()) {
            // Root nodes can be discovered when parsing jobs
            if (rootNodes.contains(base.getId())
                    || !nonRootNodes.contains(base.getId())) {
                rootNodes.add(base.getId().toString());
                Flow flow = new Flow(base.getId().toString());

                constructFlow(flow, base, visitedNodes);
                flow.initialize();
                flowMap.put(base.getId().toString(), flow);
            }
        }
    }

    /**
     * Build a flow.
     * @param flow
     * @param node
     * @param visited
     */
    private void constructFlow(Flow flow, Node node, Set<String> visited) {
        visited.add(node.getId().toString());

        flow.addNode(node);
        Map<String, Edge> dependencies = nodeDependencies.get(node.getId());

        if (dependencies != null) {
            for (Edge edge : dependencies.values()) {
                if (edge.hasError()) {
                    flow.addEdge(edge);
                } else if (visited.contains(edge.getSourceId())) {
                    // We have a cycle. We set it as an error edge
                    edge = new Edge(edge.getSourceId(), node.getId().toString());
                    edge.setError("Cyclical dependencies found.");
                    errors.add("Cyclical dependency found at " + edge.getId());
                    flow.addEdge(edge);
                } else {
                    // This should not be null
                    flow.addEdge(edge);
                    Node sourceNode = nodeMap.get(edge.getSourceId());
                    constructFlow(flow, sourceNode, visited);
                }
            }
        }

        visited.remove(node.getId());
    }

}
