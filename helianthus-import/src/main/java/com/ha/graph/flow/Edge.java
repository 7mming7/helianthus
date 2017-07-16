package com.ha.graph.flow;

import com.ha.graph.node.Node;
import lombok.Getter;
import lombok.Setter;

/**
 * User: shuiqing
 * DateTime: 17/7/10 下午3:00
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Setter
@Getter
public class Edge {

    private final String sourceId;
    private final String targetId;

    private Node source;
    private Node target;
    private String error;

    public Edge(String fromId, String toId) {
        this.sourceId = fromId;
        this.targetId = toId;
    }

    public String getId() {
        return getSourceId() + ">>" + getTargetId();
    }

    public boolean hasError() {
        return this.error != null;
    }
}
