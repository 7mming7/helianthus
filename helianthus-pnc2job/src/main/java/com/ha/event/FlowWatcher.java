package com.ha.event;

import com.ha.executor.ExecutableFlow;
import com.ha.executor.ExecutableNode;
import com.ha.executor.ExecuteStatus;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: shuiqing
 * DateTime: 17/7/14 上午11:34
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Setter
@Getter
public abstract class FlowWatcher {

    private Logger logger = LoggerFactory.getLogger(FlowWatcher.class);

    private int execId;
    private ExecutableFlow flow;
    private Map<String, BlockingStatus> map =
            new ConcurrentHashMap<String, BlockingStatus>();
    private boolean cancelWatch = false;

    public FlowWatcher(int execId) {
        this.execId = execId;
    }

    /**
     * Called to fire events to the JobRunner listeners
     *
     * @param jobId
     */
    protected synchronized void handleJobStatusChange(String jobId, ExecuteStatus status) {
        BlockingStatus block = map.get(jobId);
        if (block != null) {
            block.changeStatus(status);
        }
    }

    public synchronized BlockingStatus getBlockingStatus(String jobId) {
        if (cancelWatch) {
            return null;
        }

        ExecutableNode node = flow.getExecutableNodePath(jobId);
        if (node == null) {
            return null;
        }

        BlockingStatus blockingStatus = map.get(jobId);
        if (blockingStatus == null) {
            blockingStatus = new BlockingStatus(execId, jobId, node.getStatus());
            map.put(jobId, blockingStatus);
        }

        return blockingStatus;
    }

    public ExecuteStatus peekStatus(String jobId) {
        ExecutableNode node = flow.getExecutableNodePath(jobId);
        if (node != null) {
            return node.getStatus();
        }

        return null;
    }

    public synchronized void unblockAllWatches() {
        logger.info("Unblock all watches on " + execId);
        cancelWatch = true;

        for (BlockingStatus status : map.values()) {
            logger.info("Unblocking " + status.getJobId());
            status.changeStatus(ExecuteStatus.SKIPPED);
            status.unblock();
        }

        logger.info("Successfully unblocked all watches on " + execId);
    }

    public boolean isWatchCancelled() {
        return cancelWatch;
    }

    public abstract void stopWatcher();
}
