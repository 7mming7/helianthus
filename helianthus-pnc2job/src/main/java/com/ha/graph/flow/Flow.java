package com.ha.graph.flow;

import com.ha.entity.AbstractEntity;
import com.ha.graph.node.Node;
import com.ha.graph.project.Project;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * 调度图对象
 * User: shuiqing
 * DateTime: 17/6/1 下午4:57
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Entity
@Table(name = "t_Flow")
@Getter
@Setter
public class Flow extends AbstractEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.LAZY)
    @JoinColumn(name="projectId")
    @ForeignKey(name="fk_pf_projectId")
    @NotFound(action= NotFoundAction.IGNORE)
    private Project project;

    private String description;

    private Boolean active = true;

    @Transient
    private ArrayList<String> errors;

    @Transient
    private int numLevels = -1;

    @Transient
    private ArrayList<Node> startNodes = null;
    @Transient
    private ArrayList<Node> endNodes = null;

    @Transient
    private HashMap<String, Node> nodes = new HashMap<String, Node>();
    @Transient
    private HashMap<String, Edge> edges = new HashMap<String, Edge>();

    @Transient
    private HashMap<String, Set<Edge>> outEdges =
            new HashMap<String, Set<Edge>>();
    @Transient
    private HashMap<String, Set<Edge>> inEdges = new HashMap<String, Set<Edge>>();

    public Flow(){

    }

    public Flow(String id){
        this.id = Long.parseLong(id);
    }

    public void initialize() {
        if (startNodes == null) {
            startNodes = new ArrayList<Node>();
            endNodes = new ArrayList<Node>();
            for (Node node : nodes.values()) {
                // If it doesn't have any incoming edges, its a start node
                if (!inEdges.containsKey(node.getId())) {
                    startNodes.add(node);
                }

                // If it doesn't contain any outgoing edges, its an end node.
                if (!outEdges.containsKey(node.getId())) {
                    endNodes.add(node);
                }
            }

            for (Node node : startNodes) {
                node.setLevel(0);
                numLevels = 0;
                recursiveSetLevels(node);
            }
        }
    }

    private void recursiveSetLevels(Node node) {
        Set<Edge> edges = outEdges.get(node.getId());
        if (edges != null) {
            for (Edge edge : edges) {
                Node nextNode = nodes.get(edge.getTargetId());
                edge.setSource(node);
                edge.setTarget(nextNode);

                // We pick whichever is higher to get the max distance from root.
                int level = Math.max(node.getLevel() + 1, nextNode.getLevel());
                nextNode.setLevel(level);
                numLevels = Math.max(level, numLevels);
                recursiveSetLevels(nextNode);
            }
        }
    }

    public Node getNode(String nodeId) {
        return nodes.get(nodeId);
    }

    public int getNumLevels() {
        return numLevels;
    }

    public List<Node> getStartNodes() {
        return startNodes;
    }

    public List<Node> getEndNodes() {
        return endNodes;
    }

    public Set<Edge> getInEdges(String id) {
        return inEdges.get(id);
    }

    public Set<Edge> getOutEdges(String id) {
        return outEdges.get(id);
    }

    public void addAllNodes(Collection<Node> nodes) {
        for (Node node : nodes) {
            addNode(node);
        }
    }

    public void addNode(Node node) {
        nodes.put(node.getId().toString(), node);
    }

    public Collection<Node> getNodes() {
        return nodes.values();
    }

    public Collection<Edge> getEdges() {
        return edges.values();
    }

    public void addAllEdges(Collection<Edge> edges) {
        for (Edge edge : edges) {
            addEdge(edge);
        }
    }

    public void addError(String error) {
        if (errors == null) {
            errors = new ArrayList<String>();
        }

        errors.add(error);
    }

    public void addEdge(Edge edge) {
        String source = edge.getSourceId();
        String target = edge.getTargetId();

        if (edge.hasError()) {
            addError("Error on " + edge.getId() + ". " + edge.getError());
        }

        Set<Edge> sourceSet = getEdgeSet(outEdges, source);
        sourceSet.add(edge);

        Set<Edge> targetSet = getEdgeSet(inEdges, target);
        targetSet.add(edge);

        edges.put(edge.getId(), edge);
    }

    private Set<Edge> getEdgeSet(HashMap<String, Set<Edge>> map, String id) {
        Set<Edge> edges = map.get(id);
        if (edges == null) {
            edges = new HashSet<Edge>();
            map.put(id, edges);
        }

        return edges;
    }

    public Map<String, Node> getNodeMap() {
        return nodes;
    }

    public Map<String, Set<Edge>> getOutEdgeMap() {
        return outEdges;
    }

    public Map<String, Set<Edge>> getInEdgeMap() {
        return inEdges;
    }
}
