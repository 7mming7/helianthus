package com.ha.executor;

import com.ha.graph.node.Node;
import com.ha.utils.Props;
import com.ha.utils.TypedMapWrapper;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * 可执行的Node节点
 * User: shuiqing
 * DateTime: 17/7/10 上午11:40
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Setter
@Getter
public class ExecutableNode {

    public static final String ID_PARAM = "id";
    public static final String EXECUTESTATUSPA_PARAM = "execstatus";
    public static final String INNODES_PARAM = "inNodes";
    public static final String OUTNODES_PARAM = "outNodes";
    public static final String TYPE_PARAM = "type";

    private String id;
    private String type = null;
    private ExecuteStatus status = ExecuteStatus.READY;

    private Set<String> inNodes = new HashSet<String>();
    private Set<String> outNodes = new HashSet<String>();

    private Props inputProps;
    private Props outputProps;

    // Transient. These values aren't saved, but rediscovered.
    private ExecutableFlow parentFlow;

    public ExecutableNode(Node node){
        this.id = node.getId().toString();
    }

    public ExecutableNode(Node node, ExecutableFlow parent) {
        this(node.getId().toString(), node.getJobType(), parent);
    }

    public ExecutableNode(String id, String type, ExecutableFlow parent) {
        this.id = id;
        this.type = type;
        setParentFlow(parent);
    }

    public ExecutableNode() {
    }

    public Map<String, Object> toObject() {
        Map<String, Object> mapObj = new HashMap<String, Object>();
        fillMapFromExecutable(mapObj);

        return mapObj;
    }

    protected void fillMapFromExecutable(Map<String, Object> objMap) {
        objMap.put(ID_PARAM, this.id);
        objMap.put(EXECUTESTATUSPA_PARAM, status.toString());
        objMap.put(TYPE_PARAM, type);

        if (inNodes != null && !inNodes.isEmpty()) {
            objMap.put(INNODES_PARAM, inNodes);
        }
        if (outNodes != null && !outNodes.isEmpty()) {
            objMap.put(OUTNODES_PARAM, outNodes);
        }
    }

    public void fillExecutableFromMapObject(
            TypedMapWrapper<String, Object> wrappedMap) {
        this.id = wrappedMap.getString(ID_PARAM);
        this.type = wrappedMap.getString(TYPE_PARAM);
        this.status = ExecuteStatus.valueOf(wrappedMap.getString(EXECUTESTATUSPA_PARAM));

        this.inNodes = new HashSet<String>();
        this.inNodes.addAll(wrappedMap.getStringCollection(INNODES_PARAM,
                Collections.<String> emptySet()));

        this.outNodes = new HashSet<String>();
        this.outNodes.addAll(wrappedMap.getStringCollection(OUTNODES_PARAM,
                Collections.<String> emptySet()));

    }

    public ExecutableFlow getExecutableFlow() {
        if (parentFlow == null) {
            return null;
        }

        return parentFlow.getExecutableFlow();
    }

    public void fillExecutableFromMapObject(Map<String, Object> objMap) {
        TypedMapWrapper<String, Object> wrapper =
                new TypedMapWrapper<String, Object>(objMap);
        fillExecutableFromMapObject(wrapper);
    }

    public void cancelNode(long cancelTime) {
        if (this.status == ExecuteStatus.DISABLED) {
            skipNode(cancelTime);
        } else {
            this.setStatus(ExecuteStatus.CANCELLED);
        }
    }

    public void skipNode(long skipTime) {
        this.setStatus(ExecuteStatus.SKIPPED);
    }

    public void setParentFlow(ExecutableFlow flow) {
        this.parentFlow = flow;
    }

    public ExecutableFlow getParentFlow() {
        return parentFlow;
    }

    public void addOutNode(String exNode) {
        outNodes.add(exNode);
    }

    public void addInNode(String exNode) {
        inNodes.add(exNode);
    }

    public String getNestedId() {
        return getPrintableId(":");
    }

    public String getPrintableId(String delimiter) {
        if (this.getParentFlow() == null
                || this.getParentFlow() instanceof ExecutableFlow) {
            return getId();
        }
        return getParentFlow().getPrintableId(delimiter) + delimiter + getId();
    }
}
