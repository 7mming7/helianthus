package com.ha.event;

import com.ha.executor.ExecutableFlow;
import com.ha.executor.ExecutableNode;
import com.ha.executor.ExecuteStatus;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: shuiqing
 * DateTime: 17/7/11 下午5:23
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
/**
 * Carries an immutable snapshot of the status data, suitable for asynchronous message passing.
 */
public class EventData {

    private final ExecuteStatus executeStatus;
    private final String nestedId;

    /**
     * Creates a new EventData instance.
     *
     * @param executeStatus node status.
     * @param nestedId node id, corresponds to {@link ExecutableNode#getNestedId()}.
     */
    public EventData(ExecuteStatus executeStatus, String nestedId) {
        this.executeStatus = executeStatus;
        this.nestedId = nestedId;
    }

    /**
     * Creates a new EventData instance.
     * @param node node.
     */
    public EventData(ExecutableNode node) {
        this(node.getStatus(), node.getNestedId());
    }

    public ExecuteStatus getExecuteStatus() {
        return executeStatus;
    }

    public String getNestedId() {
        return nestedId;
    }

}